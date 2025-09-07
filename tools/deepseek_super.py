#!/usr/bin/env python3
import os, sys, json, re, subprocess, urllib.request, pathlib, shlex, time

ROOT = subprocess.check_output(["git","rev-parse","--show-toplevel"], text=True).strip()
os.chdir(ROOT)

# --- config (kept local; never sent unless you include it) ---
cfg = {}
env_file = pathlib.Path("config/deepseek.env")
if env_file.exists():
    for line in env_file.read_text(encoding="utf-8", errors="replace").splitlines():
        if not line.strip() or line.strip().startswith("#"): continue
        if "=" in line:
            k,v = line.split("=",1)
            cfg[k.strip()] = v.strip().strip('"').strip("'")
DEEPSEEK_API_KEY = os.environ.get("DEEPSEEK_API_KEY", cfg.get("DEEPSEEK_API_KEY"))
DEEPSEEK_BASE_URL= os.environ.get("DEEPSEEK_BASE_URL", cfg.get("DEEPSEEK_BASE_URL","https://api.deepseek.com"))
DEEPSEEK_MODEL   = os.environ.get("DEEPSEEK_MODEL", cfg.get("DEEPSEEK_MODEL","deepseek-coder"))
if not DEEPSEEK_API_KEY: sys.exit("Missing DEEPSEEK_API_KEY (set in config/deepseek.env).")

def inside(pth: str) -> pathlib.Path:
    p = (pathlib.Path(ROOT) / pth).resolve()
    if not str(p).startswith(str(pathlib.Path(ROOT).resolve())):
        raise ValueError("Path escapes repo: "+str(p))
    return p

def run(cmd, cwd=ROOT, timeout=900):
    return subprocess.run(cmd, cwd=cwd, text=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, timeout=timeout, shell=isinstance(cmd,str))

def call_llm(messages):
    req = urllib.request.Request(
        DEEPSEEK_BASE_URL.rstrip("/") + "/chat/completions",
        data=json.dumps({"model":DEEPSEEK_MODEL,"messages":messages,"temperature":0,"max_tokens":2000}).encode("utf-8"),
        headers={"Authorization":"Bearer "+DEEPSEEK_API_KEY, "Content-Type":"application/json"}
    )
    with urllib.request.urlopen(req, timeout=180) as r:
        data = json.loads(r.read().decode("utf-8","replace"))
    return data["choices"][0]["message"]["content"]

SYS = (
"YOU ARE A REPO SUPER-AGENT.\n"
"You can request tools by replying with pure JSON only (no fences, no prose):\n"
'{"tool":"list","path":".","depth":2}\n'
'{"tool":"read","path":"app/src/.../MainActivity.kt","max_bytes":20000}\n'
'{"tool":"grep","pattern":"ExperimentalMaterial3Api","path":"."}\n'
'{"tool":"build","cmd":"./gradlew assembleDebug --stacktrace --no-daemon"}\n'
'{"tool":"patch","diff":"<unified git diff starting with diff --git ...>"}\n'
'{"tool":"commit_push","message":"fix build"}\n'
'{"tool":"done","answer":"<final explanation>"}\n'
"Rules:\n"
"- Start with list/read/grep/build before patch.\n"
"- Patch must be a valid unified diff with a/ and b/ paths, @@ hunks, LF newlines, no code fences.\n"
"- Keep edits minimal; do NOT bump versions unless the error explicitly requires it.\n"
"- When finished, call tool=done with a short summary."
)

def sanitize_diff(s: str) -> str:
    s = s.replace("\r\n","\n").replace("\r","\n").strip()
    s = re.sub(r"^```(?:diff|patch|json)?\s*|\s*```$", "", s, flags=re.S)
    i = s.find("diff --git ")
    if i != -1: s = s[i:]
    out=[]
    for line in s.splitlines():
        if line.startswith("--- ") and not (line.startswith("--- a/") or line.startswith("--- /dev/null")):
            line = "--- a/"+line[4:].lstrip("./")
        if line.startswith("+++ ") and not (line.startswith("+++ b/") or line.startswith("+++ /dev/null")):
            line = "+++ b/"+line[4:].lstrip("./")
        out.append(line)
    return "\n".join(out).rstrip()+"\n"

def _gradle_alt_candidates(p: pathlib.Path):
    s = str(p)
    c = [p]
    if s.endswith("build.gradle.kts"):
        c.append(p.with_name("build.gradle"))
    elif s.endswith("build.gradle"):
        c.append(p.with_name("build.gradle.kts"))
    # if module path, also try root
    if "/app/" in s or str(pathlib.Path(ROOT)/"app") in s:
        c.append(pathlib.Path(ROOT)/"build.gradle")
        c.append(pathlib.Path(ROOT)/"build.gradle.kts")
    return c

def tool_list(path=".", depth=2):
    base = inside(path)
    output=[]
    for root,dirs,files in os.walk(base):
        rel = os.path.relpath(root, ROOT)
        level = rel.count(os.sep)
        if level > depth:
            dirs[:] = []
            continue
        output.append(rel if rel!="." else ".")
        for f in sorted(files)[:300]:
            output.append(os.path.join(rel,f))
    return "\n".join(output[:6000])

def tool_read(path, max_bytes=20000):
    p = inside(path)
    for cand in _gradle_alt_candidates(p):
        if cand.exists():
            p = cand
            break
    else:
        try:
            parent = p.parent
            listing = "\n".join(x.name for x in sorted(parent.iterdir(), key=lambda x: x.name)[:200]) if parent.exists() else "(no parent)"
        except Exception:
            listing = "(unavailable)"
        return f"[ERROR: not found] {path}\nNearby in {p.parent}:\n{listing}"
    try:
        data = p.read_bytes()
        if len(data)>max_bytes:
            return data[:max_bytes].decode("utf-8","replace") + f"\n\n[TRUNCATED {len(data)-max_bytes} bytes]"
        return data.decode("utf-8","replace")
    except Exception as e:
        return f"[ERROR reading {p}]: {e}\n"

def tool_grep(pattern, path="."):
    p = inside(path)
    cmd = f"grep -RIn -m 200 {shlex.quote(pattern)} {shlex.quote(str(p))}"
    return run(cmd).stdout[:20000]

def tool_build(cmd="./gradlew assembleDebug --stacktrace --no-daemon"):
    return run(cmd).stdout[-40000:]

def safety_point():
    ts=time.strftime("%Y%m%d-%H%M%S")
    run(["git","tag","-f",f"safety/{ts}"])
    run(["git","checkout","-b",f"backup/{ts}"]).stdout or run(["git","checkout",f"backup/{ts}"])
    return ts

def tool_patch(diff):
    ts = safety_point()
    patch = sanitize_diff(diff)
    tmpdir = os.environ.get("TMPDIR","/data/data/com.termux/files/usr/tmp")
    os.makedirs(tmpdir, exist_ok=True)
    pf = pathlib.Path(tmpdir)/f"ds_{ts}.patch"
    pathlib.Path(pf).write_text(patch, encoding="utf-8")
    r = run(["git","apply","--3way","--whitespace=fix",str(pf)])
    if r.returncode != 0:
        r = run(["git","apply",str(pf)])
        if r.returncode != 0:
            return f"APPLY_FAILED\nPatch at {pf}\n{r.stdout}"
    run(["git","add","-A"])
    return f"APPLIED\nPatch at {pf}"

def tool_commit_push(message):
    run(["git","commit","-m",message]).stdout
    b = run("git rev-parse --abbrev-ref HEAD").stdout.strip() or "backup/auto"
    run(["git","push","--set-upstream","origin",b]).stdout
    run(["git","push","--tags"]).stdout
    return f"PUSHED branch {b}"

def main():
    raw_args = sys.argv[1:]
    explain_cli = (len(raw_args)>0 and raw_args[0] in ("--explain","explain"))
    instr = " ".join(raw_args[1:] if explain_cli else raw_args).strip() if raw_args else sys.stdin.read()
    mode = os.environ.get("DS_MODE","auto")
    if explain_cli: mode = "explain"

    messages=[{"role":"system","content":SYS}]

    ci_tail = ""
    ci = pathlib.Path(".ops_receipts/ci_last.log")
    if ci.exists():
        t = ci.read_text(encoding="utf-8", errors="replace")
        ci_tail = "\n\n---- CI LOG TAIL ----\n" + "\n".join(t.splitlines()[-400:])

    if mode == "explain" or "[EXPLAIN_ONLY]" in instr:
        messages.append({"role":"user","content": instr + "\n\nPOLICY: Explain only. Do not request tools."})
        print(call_llm(messages).strip())
        return

    messages.append({"role":"user","content": instr + ci_tail})

    for step in range(18):
        resp = call_llm(messages).strip()
        try:
            cmd = json.loads(resp)
        except Exception:
            print(resp)
            return

        tool = cmd.get("tool")
        try:
            if tool=="list":
                out = tool_list(cmd.get("path","."), int(cmd.get("depth",2)))
                messages += [{"role":"assistant","content":resp},{"role":"user","content":"OBSERVATION:list\n"+out}]
            elif tool=="read":
                out = tool_read(cmd["path"], int(cmd.get("max_bytes",20000)))
                messages += [{"role":"assistant","content":resp},{"role":"user","content":"OBSERVATION:read\n"+out}]
            elif tool=="grep":
                out = tool_grep(cmd["pattern"], cmd.get("path","."))
                messages += [{"role":"assistant","content":resp},{"role":"user","content":"OBSERVATION:grep\n"+out}]
            elif tool=="build":
                out = tool_build(cmd.get("cmd","./gradlew assembleDebug --stacktrace --no-daemon"))
                messages += [{"role":"assistant","content":resp},{"role":"user","content":"OBSERVATION:build\n"+out}]
            elif tool=="patch":
                out = tool_patch(cmd["diff"])
                messages += [{"role":"assistant","content":resp},{"role":"user","content":"OBSERVATION:patch\n"+out}]
            elif tool=="commit_push":
                out = tool_commit_push(cmd.get("message","[agent] changes"))
                messages += [{"role":"assistant","content":resp},{"role":"user","content":"OBSERVATION:commit\n"+out}]
            elif tool=="done":
                print(cmd.get("answer","DONE"))
                return
            else:
                print("Unknown tool or malformed JSON:", resp)
                return
        except Exception as e:
            messages += [{"role":"assistant","content":resp},{"role":"user","content":f"OBSERVATION:error\n{type(e).__name__}: {e}"}]

    print("Stopped after max steps.")

if __name__ == "__main__":
    main()

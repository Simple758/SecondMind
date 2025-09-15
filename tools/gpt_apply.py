#!/usr/bin/env python3
import os, sys, json, subprocess, pathlib, textwrap
import regex

ROOT = pathlib.Path(__file__).resolve().parents[1]

TEMPLATE_SYS = """You are a cautious code-editing planner.
Return ONLY JSON like: {"version":"1.0","steps":[ ... ]}
Allowed ops:
- {"op":"replace","file":"path","match":"REGEX","text":"NEW"}
- {"op":"insert_after","file":"path","match":"REGEX","text":"TEXT"}
- {"op":"insert_before","file":"path","match":"REGEX","text":"TEXT"}
- {"op":"delete","file":"path","match":"REGEX"}
Rules:
- Use precise REGEX that uniquely anchors the location.
- Avoid duplicate imports/lines; keep code compilable.
- If unsure, include // TODO notes in TEXT.
- No prose, only JSON.
"""

def sh(cmd, check=True):
    print("+", " ".join(cmd))
    p = subprocess.run(cmd, text=True, capture_output=True)
    if p.stdout: print(p.stdout)
    if p.stderr: print(p.stderr, file=sys.stderr)
    if check and p.returncode != 0: sys.exit(p.returncode)
    return p

def list_files(max_chars=8000):
    files = sh(["git","ls-files"]).stdout.strip().splitlines()
    snips, budget = [], max_chars
    for f in files:
        if not any(f.endswith(ext) for ext in (".kt",".java",".xml",".gradle",".kts",".md",".json",".yaml",".yml",".txt")):
            continue
        p = ROOT / f
        try:
            t = p.read_text(encoding="utf-8", errors="ignore")
        except Exception:
            continue
        block = f"\n=== {f} ===\n" + "\n".join(t.splitlines()[:80]) + "\n"
        if len(block) <= budget:
            snips.append(block)
            budget -= len(block)
    return files, "".join(snips)

def call_llm(model, instruction, files, snips):
    prefer_deepseek = bool(os.getenv("DEEPSEEK_API_KEY"))
    import urllib.request
    import json as _json

    if prefer_deepseek:
        url = "https://api.deepseek.com/chat/completions"
        headers = {"Content-Type":"application/json","Authorization":f"Bearer {os.environ['DEEPSEEK_API_KEY']}"}
        payload = {
            "model": model or "deepseek-chat",
            "messages": [
                {"role":"system","content":TEMPLATE_SYS},
                {"role":"user","content": textwrap.dedent(f"""
                    Instruction:
                    {instruction}

                    File list (truncated):
                    {_json.dumps(files[:200])}

                    Snippets:
                    {snips}
                """)}
            ],
            "temperature": 0.2
        }
    else:
        url = "https://api.openai.com/v1/chat/completions"
        headers = {"Content-Type":"application/json","Authorization":f"Bearer {os.environ['OPENAI_API_KEY']}"}
        payload = {
            "model": model or "gpt-4.1-mini",
            "messages": [
                {"role":"system","content":TEMPLATE_SYS},
                {"role":"user","content": textwrap.dedent(f"""
                    Instruction:
                    {instruction}

                    File list (truncated):
                    {_json.dumps(files[:200])}

                    Snippets:
                    {snips}
                """)}
            ],
            "temperature": 0.2
        }

    req = urllib.request.Request(url, headers=headers, data=_json.dumps(payload).encode("utf-8"))
    with urllib.request.urlopen(req) as r:
        data = _json.loads(r.read().decode("utf-8"))
    return data["choices"][0]["message"]["content"].strip()

def apply_step(step):
    path = ROOT / step["file"]
    if not path.exists(): raise FileNotFoundError(step["file"])
    original = path.read_text(encoding="utf-8", errors="ignore")
    pat = regex.compile(step["match"], regex.DOTALL)

    op = step["op"]
    if op == "replace":
        new = pat.sub(step["text"], original, count=1)
    elif op == "insert_after":
        m = pat.search(original)
        if not m: raise ValueError(f"Pattern not found (after): {step['file']}")
        pos = m.end()
        ins = step["text"]
        if ins.strip() in original: return False
        new = original[:pos] + ("\n" if original[pos:pos+1]!="\n" else "") + ins + original[pos:]
    elif op == "insert_before":
        m = pat.search(original)
        if not m: raise ValueError(f"Pattern not found (before): {step['file']}")
        pos = m.start()
        ins = step["text"]
        if ins.strip() in original: return False
        new = original[:pos] + ins + ("\n" if original[max(0,pos-1):pos]!="\n" else "") + original[pos:]
    elif op == "delete":
        new = pat.sub("", original, count=1)
    else:
        raise ValueError(f"Unsupported op: {op}")

    if new == original: return False
    path.write_text(new, encoding="utf-8")
    print(f"= Applied {op} -> {step['file']}")
    return True

def main():
    # Inputs
    instruction = os.environ.get("INSTRUCTION","")
    if not instruction and "--instruction" in sys.argv:
        instruction = sys.argv[sys.argv.index("--instruction")+1]
    model = os.environ.get("MODEL")
    author = "gpt-bot"; email = "actions@users.noreply.github.com"
    if "--commit-author" in sys.argv:
        author = sys.argv[sys.argv.index("--commit-author")+1]
    if "--commit-email" in sys.argv:
        email = sys.argv[sys.argv.index("--commit-email")+1]

    files, snips = list_files()
    content = call_llm(model, instruction, files, snips)
    js = content[content.find("{"):content.rfind("}")+1]
    plan = json.loads(js)
    assert plan.get("version") == "1.0"
    changed = False
    for st in plan.get("steps", []):
        if apply_step(st): changed = True

    if not changed:
        print("No changes to commit.")
        return
    sh(["git","config","user.name",author])
    sh(["git","config","user.email",email])
    sh(["git","add","-A"])
    sh(["git","diff","--cached","--stat"], check=False)
    sh(["git","commit","-m",f"GPT apply: {instruction[:72]}"])
    sh(["git","push"])
if __name__ == "__main__":
    main()

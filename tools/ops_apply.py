#!/usr/bin/env python3
import sys, json, re, pathlib
from typing import List

ROOT = pathlib.Path(".").resolve()

def read(path): return pathlib.Path(path).read_text(encoding="utf-8")
def write(path, data):
    p = pathlib.Path(path); p.parent.mkdir(parents=True, exist_ok=True)
    p.write_text(data, encoding="utf-8")

def ensure_balanced(s: str) -> bool:
    stack=[]; pairs={')':'(',']':'[','}':'{'}
    for ch in s:
        if ch in '([{': stack.append(ch)
        elif ch in ')]}':
            if not stack or stack[-1]!=pairs[ch]: return False
            stack.pop()
    return True

def dedup_imports(content: str) -> str:
    seen=set(); out=[]
    for line in content.splitlines(True):
        if line.startswith("import "):
            if line in seen: continue
            seen.add(line)
        out.append(line)
    return "".join(out)

ALLOWED_PREFIXES = [
    "app/src/", "app/build.gradle", "build.gradle", "settings.gradle",
    "gradle.properties", ".github/workflows/"
]

def apply_step(step, content):
    op = step["op"]
    if op == "open":
        return content
    if op == "ensure_import":
        imp = step["import"].strip()
        if imp not in content:
            m = re.search(r"^package .+$", content, re.M)
            if m:
                idx = m.end()
                content = content[:idx] + "\nimport " + imp + "\n" + content[idx:]
            else:
                content = "import " + imp + "\n" + content
        return dedup_imports(content)
    if op == "insert":
        txt = step.get("text","")
        guard = step.get("guard","")
        if guard and re.search(guard, content, re.S):
            return content
        if "after" in step:
            anchor = step["after"]; pos = content.find(anchor)
            if pos==-1: raise RuntimeError(f"after anchor not found: {anchor}")
            insert_pos = pos + len(anchor)
            content = content[:insert_pos] + txt + content[insert_pos:]
        elif "before" in step:
            anchor = step["before"]; pos = content.find(anchor)
            if pos==-1: raise RuntimeError(f"before anchor not found: {anchor}")
            content = content[:pos] + txt + content[pos:]
        elif "at_line" in step:
            lines = content.splitlines(True)
            i = max(0, min(len(lines), step["at_line"]-1))
            lines.insert(i, txt); content = "".join(lines)
        else:
            raise RuntimeError("insert requires after/before/at_line")
        return content
    if op == "replace":
        pat = re.compile(step["pattern"], re.S)
        content, n = pat.subn(step.get("with",""), content, count=1)
        if n==0: raise RuntimeError(f"replace pattern not found: {step['pattern']}")
        return content
    if op == "move_block":
        s = content.find(step["block_start"]); e = content.find(step["block_end"])
        if s==-1 or e==-1 or e<=s: raise RuntimeError("block anchors not found or invalid")
        e += len(step["block_end"]); block = content[s:e]
        content = content[:s] + content[e:]
        if "after" in step:
            pos = content.find(step["after"]); 
            if pos==-1: raise RuntimeError("move after anchor not found")
            content = content[:pos+len(step["after"])] + block + content[pos+len(step["after"]):]
        elif "before" in step:
            pos = content.find(step["before"]);
            if pos==-1: raise RuntimeError("move before anchor not found")
            content = content[:pos] + block + content[pos:]
        else:
            raise RuntimeError("move_block requires after/before")
        return content
    raise RuntimeError(f"Unsupported op: {op}")

def main():
    if len(sys.argv)<2:
        print("Usage: ops_apply.py plan.json", file=sys.stderr); sys.exit(2)
    plan_path = pathlib.Path(sys.argv[1])
    plan = json.loads(plan_path.read_text(encoding="utf-8"))
    steps = plan.get("steps", [])
    receipts_dir = ROOT/".ops_receipts"; receipts_dir.mkdir(exist_ok=True)
    touched: List[str] = []
    for step in steps:
        path = step["file"]
        if not any(path.startswith(p) for p in ALLOWED_PREFIXES):
            raise RuntimeError(f"Path not allowed: {path}")
        if step["op"]=="open": 
            _ = read(path); continue
        data = read(path)
        new = apply_step(step, data)
        if not ensure_balanced(new):
            raise RuntimeError(f"Unbalanced braces/parentheses after {path}")
        if new != data:
            write(path, new)
            if path not in touched: touched.append(path)
    (receipts_dir/"last.json").write_text(json.dumps(plan, indent=2), encoding="utf-8")
    print("Touched files:")
    for t in touched: print(" -", t)
    print("OK")
if __name__=="__main__": main()

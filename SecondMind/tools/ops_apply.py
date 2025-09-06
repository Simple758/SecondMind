#!/usr/bin/env python3
import sys, json, re, pathlib
from typing import List

ROOT = pathlib.Path(".").resolve()

def read(path): return pathlib.Path(path).read_text(encoding="utf-8")
def write(path, data): pathlib.Path(path).write_text(data, encoding="utf-8")

def ensure_balanced(s: str) -> bool:
    stack, pairs = [], {')':'(',']':'[','}':'{'}
    for ch in s:
        if ch in '([{': stack.append(ch)
        elif ch in ')]}':
            if not stack or stack[-1]!=pairs[ch]: return False
            stack.pop()
    return True

def dedup_imports(content: str) -> str:
    seen = set(); out = []
    for line in content.splitlines(True):
        if line.startswith("import "):
            if line in seen: continue
            seen.add(line)
        out.append(line)
    return "".join(out)

def guard_blocks(content: str, guard) -> bool:
    if not guard: return False
    if isinstance(guard, dict) and "notContains" in guard:
        return (guard["notContains"] in content)
    # treat as regex string
    try:
        return re.search(str(guard), content, re.S) is not None
    except re.error:
        return False

def apply_step(step, content):
    op = step["op"]

    # alias mapping
    if "line" in step and "text" not in step:
        step["text"] = "\n" + step["line"].rstrip() + "\n"

    if op == "open":
        return content

    if op == "ensure_import":
        imp = step["import"].strip()
        if ("import " + imp) not in content:
            m = re.search(r"^package .+$", content, re.M)
            if m:
                idx = m.end()
                content = content[:idx] + "\nimport " + imp + "\n" + content[idx:]
            else:
                content = "import " + imp + "\n" + content
        return dedup_imports(content)

    if op == "insert":
        txt = step.get("text","")
        if guard_blocks(content, step.get("guard")):
            return content
        if "after" in step:
            anchor = step["after"]
            pos = content.find(anchor)
            if pos==-1: raise RuntimeError(f"after anchor not found: {anchor}")
            insert_pos = pos + len(anchor)
            content = content[:insert_pos] + txt + content[insert_pos:]
        elif "before" in step:
            anchor = step["before"]
            pos = content.find(anchor)
            if pos==-1: raise RuntimeError(f"before anchor not found: {anchor}")
            content = content[:pos] + txt + content[pos:]
        elif "at_line" in step:
            lines = content.splitlines(True)
            i = max(0, min(len(lines), step["at_line"]-1))
            lines.insert(i, txt)
            content = "".join(lines)
        else:
            raise RuntimeError("insert requires after/before/at_line")
        return content

    if op == "replace":
        pat = re.compile(step["pattern"], re.S)
        content, n = pat.subn(step.get("with",""), content, count=1)
        if n==0: raise RuntimeError(f"replace pattern not found: {step['pattern']}")
        return content

    if op == "move_block":
        start = content.find(step["block_start"])
        end = content.find(step["block_end"])
        if start==-1 or end==-1 or end<=start: raise RuntimeError("block anchors not found or invalid")
        end += len(step["block_end"])
        block = content[start:end]
        content = content[:start] + content[end:]
        if "after" in step:
            pos = content.find(step["after"])
            if pos==-1: raise RuntimeError("move after anchor not found")
            insert_pos = pos + len(step["after"])
            content = content[:insert_pos] + block + content[insert_pos:]
        elif "before" in step:
            pos = content.find(step["before"])
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

    # accept either "steps" or "ops"
    steps = plan.get("steps")
    if steps is None and "ops" in plan:
        steps = plan["ops"]

    if not isinstance(steps, list):
        print("Plan missing steps/ops list", file=sys.stderr); sys.exit(2)

    receipts_dir = ROOT/".ops_receipts"
    receipts_dir.mkdir(exist_ok=True)
    touched: List[str] = []
    for step in steps:
        path = step["file"]
        if not any(path.startswith(p) for p in [
            "app/src/", "app/build.gradle", "build.gradle", "settings.gradle", "SecondMind/app/src/", "SecondMind/app/build.gradle", "SecondMind/build.gradle", "SecondMind/settings.gradle",
            "gradle.properties", ".github/workflows/"
        ]):
            raise RuntimeError(f"Path not allowed: {path}")
        if step["op"]=="open":  # no write
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

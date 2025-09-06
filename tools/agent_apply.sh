#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
. "$ROOT/ops/agent.env"

PLAN="${1:-latest}"
if [[ "$PLAN" == "latest" ]]; then
  PLAN=$(ls -t "$ROOT/ops/plans"/*.json | head -n1)
fi
NORM="$ROOT/ops/normalized/$(basename "$PLAN")"

python3 - "$PLAN" "$NORM" << "PY"
import sys, json, pathlib, re
inp, out = sys.argv[1], sys.argv[2]
raw = pathlib.Path(inp).read_text(encoding="utf-8").strip()
raw = raw.replace("```json","").replace("```","")
data = json.loads(raw)
ops = data.get("ops") or data.get("steps") or data.get("plan") or []
fixed=[]
for o in ops:
    o=dict(o)
    if "old" in o and "oldValue" not in o: o["oldValue"]=o.pop("old")
    if "new" in o and "newValue" not in o: o["newValue"]=o.pop("new")
    if "file" in o and "path" not in o:    o["path"]=o["file"]
    if "content" in o and "value" not in o: o["value"]=o.pop("content")
    if isinstance(o.get("guard"), str):    o["guard"]={"pattern": o["guard"]}
    if "anchor" in o and not any(k in o for k in ("after","before")): o["after"]=True
    fixed.append(o)
pathlib.Path(out).write_text(json.dumps({"ops":fixed},indent=2), encoding="utf-8")
print("Normalized plan at", out)
PY

AUTO_YES=1 tools/agentctl apply
cp "$NORM" "$ROOT/ops/receipts/$(basename "$NORM")"

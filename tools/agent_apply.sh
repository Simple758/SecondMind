#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
. "$ROOT/ops/agent.env"
TMPDIR="${TMPDIR:-/data/data/com.termux/files/usr/tmp}"

PLAN_ARG="${1:-latest}"
if [[ "$PLAN_ARG" == "latest" ]]; then
  PLAN_IN="$(ls -t "$ROOT/ops/plans"/*.json 2>/dev/null | head -n1)"
  [[ -z "${PLAN_IN:-}" ]] && { echo "No plans found in ops/plans"; exit 2; }
else
  PLAN_IN="$PLAN_ARG"
fi

mkdir -p "$ROOT/ops/normalized" "$ROOT/ops/receipts"
NORM="$ROOT/ops/normalized/$(basename "$PLAN_IN")"
PLAN_TMP="$TMPDIR/plan.json"

python3 - "$PLAN_IN" "$NORM" << "PY"
import sys, json, pathlib, re
inp, out = sys.argv[1], sys.argv[2]
raw = pathlib.Path(inp).read_text(encoding="utf-8").strip().replace("```json","").replace("```","")
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
pathlib.Path(out).write_text(json.dumps({"ops":fixed}, ensure_ascii=False, indent=2), encoding="utf-8")
print("Normalized plan at", out)
PY

# Copy normalized plan into engine expected location and apply non-interactively
cp -f "$NORM" "$PLAN_TMP"
AUTO_YES=1 tools/agentctl apply

# Archive receipt
cp -f "$NORM" "$ROOT/ops/receipts/$(basename "$NORM")"
echo "Applied normalized plan: $NORM"

#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
. "$ROOT/ops/agent.env"
TMPDIR="${TMPDIR:-/data/data/com.termux/files/usr/tmp}"
mkdir -p "$ROOT/ops/plans" "$ROOT/ops/logs"
TS="$(date +%s)"
PLAN_REPO="$ROOT/ops/plans/${TS}.json"
PLAN_TMP="$TMPDIR/plan.json"
LOG="$ROOT/ops/logs/ci_tail_${TS}.log"

# Pull last CI tail if present (non-fatal)
tail -n 400 .ops_receipts/ci_last.log > "$LOG" 2>/dev/null || true

INSTR=$(
  printf "%s\n" \
  "Fix the CI build failure using the log below." \
  "Make the smallest safe changes (Gradle/settings/workflow);" \
  "no version bumps unless strictly required." \
  "Return ONLY JSON ops." \
  "" "---- CI LOG ----" "$(cat "$LOG")"
)

OUT="$(mktemp)"
tools/agentctl plan "$INSTR" | tee "$OUT" >/dev/null || true

# Prefer an explicit file path if the planner printed it
PATH_HINT="$(grep -oE "Plan at [^ ]+" "$OUT" | awk "{print \$3}")"
if [[ -n "$PATH_HINT" && -s "$PATH_HINT" ]]; then
  cp -f "$PATH_HINT" "$PLAN_REPO"
  cp -f "$PATH_HINT" "$PLAN_TMP"
else
  # Extract JSON block from stdout and write to both locations
  python3 - "$OUT" "$PLAN_REPO" "$PLAN_TMP" << "PY" || true
import sys, json, re, pathlib
out, repo, tmp = sys.argv[1], sys.argv[2], sys.argv[3]
s = pathlib.Path(out).read_text(encoding="utf-8")
m = re.search(r"```json\s*(\{.*?\})\s*```", s, flags=re.S) or re.search(r"(\{.*\})\s*$", s, flags=re.S)
if not m:
    raise SystemExit(1)
for dest in (repo, tmp):
    pathlib.Path(dest).write_text(m.group(1), encoding="utf-8")
print("WROTE_FROM_STDOUT")
PY
fi

echo "Plan saved:"
ls -lh "$PLAN_REPO" 2>/dev/null || true
ls -lh "$PLAN_TMP"  2>/dev/null || true

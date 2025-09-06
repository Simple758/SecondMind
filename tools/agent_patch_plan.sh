#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
[[ -f "$ROOT/config/deepseek.env" ]] && . "$ROOT/config/deepseek.env"
: "${DEEPSEEK_MODEL:=deepseek-coder}"

TS="$(date +%s)"
LOG="$ROOT/ops/logs/ci_tail_${TS}.log"
PATCH="$ROOT/ops/patches/${TS}.patch"

# Context for the model (non-fatal if missing)
tail -n 400 "$ROOT/.ops_receipts/ci_last.log" > "$LOG" 2>/dev/null || true

read -r -d "" INSTR << "TXT" || true
You are a code-fixing assistant. Produce a SINGLE unified diff (git patch)
that fixes the Android CI build failure shown below. Rules:
- Only minimal changes necessary to make the build pass.
- Prefer edits in Gradle/settings/workflow or precise Kotlin imports/annotations.
- NO version bumps unless strictly required by the error.
- Output MUST be a valid unified diff starting with lines like:
  diff --git a/<path> b/<path>
  --- a/<path>
  +++ b/<path>
  @@
No prose, no markdown fences, just the patch.
TXT

tools/agentctl plan "$INSTR

---- CI LOG (tail) ----
$(cat "$LOG")" | sed "s/\r$//" > "$PATCH" || true

# If the model printed a path like "Patch at ...", try to grab it
if ! grep -q "^diff --git " "$PATCH" 2>/dev/null; then
  HINT=$(grep -oE "Patch at [^ ]+" "$PATCH" | awk "{print \$3}")
  [[ -n "$HINT" && -s "$HINT" ]] && cp -f "$HINT" "$PATCH" || true
fi

echo "Patch saved to: $PATCH"
sed -n "1,40p" "$PATCH" || true

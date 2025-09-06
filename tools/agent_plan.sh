#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
. "$ROOT/ops/agent.env"

PLAN="$ROOT/ops/plans/$(date +%s).json"
LOG="$ROOT/ops/logs/ci_tail_$(date +%s).log"

# grab last 400 lines of CI log
tail -n 400 .ops_receipts/ci_last.log > "$LOG" 2>/dev/null || true

INSTR="Fix the CI build failure using the log below. 
Make the smallest safe changes (Gradle/settings/workflow), 
no version bumps unless strictly required. 
Return ONLY JSON ops.

---- CI LOG ----
$(cat "$LOG")"

tools/agentctl plan "$INSTR" > "$PLAN" || true
echo "Plan saved to $PLAN"

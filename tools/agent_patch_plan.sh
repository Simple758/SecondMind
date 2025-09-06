#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
[[ -f "$ROOT/config/deepseek.env" ]] && . "$ROOT/config/deepseek.env"
: "${DEEPSEEK_MODEL:=deepseek-coder}"

TS="$(date +%s)"
LOG="$ROOT/ops/logs/ci_tail_${TS}.log"
RAW="$ROOT/ops/patches/${TS}.raw"
PATCH="$ROOT/ops/patches/${TS}.patch"

mkdir -p "$ROOT/ops/logs" "$ROOT/ops/patches"

# Context (non-fatal if missing)
tail -n 400 "$ROOT/.ops_receipts/ci_last.log" > "$LOG" 2>/dev/null || true

MSG="$(printf "%s\n\n---- CI LOG (tail) ----\n%s\n" \
  "You are a code-fixing assistant. Produce a SINGLE unified diff (git patch) that fixes the Android CI build failure." \
  "$(cat "$LOG" 2>/dev/null)")"

RULES=$'Rules:
- Only minimal changes necessary to make the build pass.
- Prefer edits in Gradle/settings/workflow or precise Kotlin imports/annotations.
- NO version bumps unless strictly required by the error.
- Output MUST be a valid unified diff starting with:
  diff --git a/<path> b/<path>
  --- a/<path>
  +++ b/<path>
  @@
No prose, no markdown fences, just the patch.'

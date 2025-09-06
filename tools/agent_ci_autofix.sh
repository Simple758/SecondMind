#!/usr/bin/env bash
set -euo pipefail

# --- env & repo ---
if [[ -f config/deepseek.env ]]; then set -a; . config/deepseek.env; set +a; fi
: "${DEEPSEEK_MODEL:=deepseek-coder}"
if ! command -v gh >/dev/null 2>&1; then echo "❌ gh not found"; exit 1; fi
if [[ -n "${REPO_FULL:-}" ]]; then REPO="$REPO_FULL"; else origin=$(git remote get-url origin 2>/dev/null||true); REPO=$(printf "%s" "$origin"|sed -E "s#.*github.com[:/]|\.git$##"); [[ -z "$REPO"||"$REPO" == *" "* ]] && REPO=$(gh repo view --json nameWithOwner --jq .nameWithOwner 2>/dev/null||true); fi
if [[ -z "$REPO" ]]; then echo "❌ Could not determine repo slug"; exit 1; fi
: "${CI_WORKFLOW_NAME:=Android APK}"
run_id=$(gh run list --repo "$REPO" --workflow "$CI_WORKFLOW_NAME" -L 1 --json databaseId --jq ".[0].databaseId" 2>/dev/null)
[[ -z "$run_id" ]] && run_id=$(gh run list --repo "$REPO" -L 1 --json databaseId --jq ".[0].databaseId" 2>/dev/null)
if [[ -z "$run_id" ]]; then echo "❌ No workflow runs found"; exit 1; fi
mkdir -p .ops_receipts
gh run view "$run_id" --repo "$REPO" --log > .ops_receipts/ci_last.log || true
tail -n 400 .ops_receipts/ci_last.log > .ops_receipts/ci_last_tail.log || true
INSTR=$'Fix the CI build failure based on the log below. Produce a minimal, safe JSON ops plan.\n\
Hard constraints:\n\
- Only modify files necessary to make CI build succeed.\n\
- Prefer editing Gradle files, module build.gradle, settings.gradle, .github/workflows/*.\n\
- Do NOT bump AGP/Kotlin/Compose versions unless strictly required.\n\
- Keep changes idempotent and small. No new modules.\n\
Return ONLY the JSON ops.\n\n\
---- CI LOG TAIL (last 400 lines) ----\n'"$(cat .ops_receipts/ci_last_tail.log)"

# --- plan + apply non-interactively ---
echo "== Planning CI auto-fix with model: $DEEPSEEK_MODEL =="
tools/agentctl plan "$INSTR"

echo "== Applying plan (non-interactive) =="
if command -v yes >/dev/null 2>&1; then
  yes y | tools/agentctl apply
else
  (printf 'y\n') | tools/agentctl apply
fi

# --- summarize what changed ---
echo "== Summary =="
echo "Model: $DEEPSEEK_MODEL"
echo "Changed files:"
git --no-pager diff --name-only

echo "Diff stats:"
git --no-pager diff --shortstat || true

# --- commit & push ---
git add -A
TOP_ERR="$(grep -E -m1 'FAILURE:|What went wrong:|> Task|Plugin \[id' .ops_receipts/ci_last.log || true)"
git commit -m "[agent-ci-fix] auto-fix build; ${TOP_ERR:0:120}" || true
git push || true

echo "Pushed. CI will re-run on GitHub."

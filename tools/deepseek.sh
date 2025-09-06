#!/usr/bin/env bash
# Repo-resident DeepSeek agent (auto-apply).
# Usage:
#   tools/deepseek.sh "Fix the CI failure"
#   tools/deepseek.sh "Edit MainActivity.kt: add a Share button"
#   echo "Explain AccessibilitySettingsActivity.kt" | tools/deepseek.sh
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

# Load config (keep your key here): config/deepseek.env
[[ -f "$ROOT/config/deepseek.env" ]] && . "$ROOT/config/deepseek.env" || true
: "${DEEPSEEK_API_KEY:?Missing DEEPSEEK_API_KEY. Put it in config/deepseek.env}"
: "${DEEPSEEK_BASE_URL:=https://api.deepseek.com}"
: "${DEEPSEEK_MODEL:=deepseek-coder}"

err(){ printf "❌ %s\n" "$*" >&2; exit 1; }
say(){ printf "%s\n" "$*"; }
TS(){ date +%Y%m%d-%H%M%S; }

git rev-parse --is-inside-work-tree >/dev/null 2>&1 || err "Not a git repo."

# Instruction from args or stdin
if [[ $# -gt 0 ]]; then INSTR="$*"; else INSTR="$(cat)"; fi

# System prompt: emit patch for code changes, plain text for explanations
read -r -d "" SYS <<EOS || true
You live inside this repository and can change files.
- If the user asks to MODIFY CODE, return ONLY a single unified git patch starting with lines like:
  diff --git a/<path> b/<path>
  --- a/<path>
  +++ b/<path>
  @@
  No prose, no markdown fences.
- If the user asks to EXPLAIN or ANSWER only, return plain text (no patch).
- Keep diffs minimal and correct. Prefer tiny, surgical edits.
EOS

# Call DeepSeek via Python (safe quoting via env)
export DS_SYS="$SYS" DS_USER="$INSTR"
OUTFILE="$(mktemp)"
python3 - "$DEEPSEEK_BASE_URL" "$DEEPSEEK_API_KEY" "$DEEPSEEK_MODEL" "$OUTFILE" <<PY
import os, sys, json, urllib.request, pathlib
base, key, model, outfile = sys.argv[1:5]
system_text = os.environ.get("DS_SYS","")
user_text   = os.environ.get("DS_USER","")
req = urllib.request.Request(
    base.rstrip("/") + "/chat/completions",
    data=json.dumps({
        "model": model,
        "messages": [
            {"role":"system","content": system_text},
            {"role":"user","content": user_text}
        ],
        "temperature": 0,
        "max_tokens": 2400
    }).encode("utf-8"),
    headers={"Authorization": "Bearer "+key, "Content-Type": "application/json"}
)
with urllib.request.urlopen(req, timeout=180) as r:
    data = json.loads(r.read().decode("utf-8", "replace"))
text = data["choices"][0]["message"]["content"]
pathlib.Path(outfile).write_text(text, encoding="utf-8")
print(outfile)
PY

MODEL_OUT_PATH="$(cat "$OUTFILE")"

# Try to extract a unified diff (strip fences; cut from first "diff --git")
PATCH="$(mktemp)"
python3 - "$MODEL_OUT_PATH" "$PATCH" <<PY || true
import sys, re, pathlib
src, dest = sys.argv[1], sys.argv[2]
s = pathlib.Path(src).read_text(encoding="utf-8")
s = re.sub(r"^```(?:diff|patch|json)?\s*|\s*```$", "", s, flags=re.S)
i = s.find("diff --git ")
if i == -1:
    raise SystemExit(1)
pathlib.Path(dest).write_text(s[i:], encoding="utf-8")
PY

if [[ -s "$PATCH" ]] && grep -q "^diff --git " "$PATCH"; then
  SP="safety/$(TS)"; BR="backup/$(TS)"
  git tag -f "$SP" >/dev/null 2>&1 || true
  git checkout -b "$BR" >/dev/null 2>&1 || git checkout "$BR" >/dev/null 2>&1 || true
  say "Safety point: $SP  (branch $BR)"
  if ! git apply --3way --whitespace=fix "$PATCH"; then
    err "git apply failed. Patch saved at: $PATCH"
  fi
  git add -A
  git commit -m "[deepseek] auto-apply: ${INSTR:0:80}" || true
  git push --set-upstream origin "$BR" || true
  git push --tags || true
  say "✅ Patch applied"
  echo "Changed files:"; git --no-pager diff --name-only HEAD~1..HEAD || true
  echo "Diff stats:";   git --no-pager diff --shortstat HEAD~1..HEAD  || true
else
  say "🗣️ DeepSeek:"
  cat "$MODEL_OUT_PATH"
fi

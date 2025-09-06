#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

# Config: prefer repo env, then ops env, else shell
[[ -f "$ROOT/config/deepseek.env" ]] && . "$ROOT/config/deepseek.env" || true
[[ -f "$ROOT/ops/agent.env"      ]] && . "$ROOT/ops/agent.env"      || true
: "${DEEPSEEK_API_KEY:?Set DEEPSEEK_API_KEY in config/deepseek.env or ops/agent.env}"
: "${DEEPSEEK_BASE_URL:=https://api.deepseek.com}"
: "${DEEPSEEK_MODEL:=deepseek-coder}"

TS(){ date +%Y%m%d-%H%M%S; }
PATCH_DIR="$ROOT/.agent/patches"; LOG_DIR="$ROOT/.agent/logs"
mkdir -p "$PATCH_DIR" "$LOG_DIR"

say(){ printf "%s\n" "$*"; }
err(){ printf "❌ %s\n" "$*" >&2; exit 1; }

safety_point(){
  local t; t="$(TS)"
  git tag -f "safety/$t" >/dev/null 2>&1 || true
  git checkout -b "backup/$t" >/dev/null 2>&1 || git checkout "backup/$t" >/dev/null 2>&1 || true
  echo "$t"
}

changed_summary(){
  echo "Changed files:"; git --no-pager diff --name-only || true
  echo "Diff stats:";   git --no-pager diff --shortstat  || true
}

api_chat_args(){ # args: <system> <user>  -> prints assistant content
  python3 - "$DEEPSEEK_BASE_URL" "$DEEPSEEK_API_KEY" "$DEEPSEEK_MODEL" "$1" "$2" << "PY"
import json, sys, urllib.request
base, key, model, system_text, user_text = sys.argv[1:6]
req = urllib.request.Request(
    base.rstrip("/") + "/chat/completions",
    data=json.dumps({
        "model": model,
        "messages": [
            {"role":"system","content": system_text},
            {"role":"user","content": user_text}
        ],
        "temperature": 0,
        "max_tokens": 1800
    }).encode(),
    headers={"Authorization":"Bearer "+key, "Content-Type":"application/json"}
)
with urllib.request.urlopen(req, timeout=120) as r:
    data = json.loads(r.read().decode())
print(data["choices"][0]["message"]["content"])
PY
}

extract_patch(){ # reads stdin -> prints unified diff
  python3 - << "PY"
import sys, re
s = sys.stdin.read().strip()
# Remove common fences if any
s = re.sub(r"^```(?:diff|patch)?\s*|\s*```$", "", s, flags=re.S)
# Cut from first diff header
i = s.find("diff --git ")
if i == -1:
    print("No unified diff found in model output.", file=sys.stderr)
    sys.exit(1)
print(s[i:])
PY
}

plan_ci(){ # returns patch file path
  local log="$LOG_DIR/ci_tail_$(TS).log"
  tail -n 400 "$ROOT/.ops_receipts/ci_last.log" > "$log" 2>/dev/null || true
  local sys="You are a senior Android/Kotlin build fixer. Output ONLY a unified git patch (no prose, no fences). Minimal changes to make CI green. Prefer Gradle/settings/workflow edits or precise Kotlin imports/annotations. No version bumps unless error requires it."
  local usr
  usr="$(printf "CI build failed. Here is the tail of the log:\n\n%s\n\nCreate ONE unified diff patch that fixes the failure." "$(cat "$log" 2>/dev/null)")"
  local out
  out="$(api_chat_args "$sys" "$usr" | extract_patch)" || err "Model did not return a valid patch."
  local pf="$PATCH_DIR/$(TS).patch"; printf "%s\n" "$out" > "$pf"; echo "$pf"
}

plan_freeform(){ # args: instruction -> returns patch path
  local instr="$*"
  local sys="You are a precise code-editing assistant. Output ONLY a unified git patch. Keep diffs minimal and correct. No prose, no fences."
  local usr
  usr="$(printf "Apply the following change to this repository:\n\n%s\n\nReturn ONE unified diff patch." "$instr")"
  local out
  out="$(api_chat_args "$sys" "$usr" | extract_patch)" || err "Model did not return a valid patch."
  local pf="$PATCH_DIR/$(TS).patch"; printf "%s\n" "$out" > "$pf"; echo "$pf"
}

apply_patch(){ # arg: patch path or latest
  local pf="${1:-latest}"
  [[ "$pf" == "latest" ]] && pf="$(ls -t "$PATCH_DIR"/*.patch 2>/dev/null | head -n1 || true)"
  [[ -n "$pf" && -s "$pf" ]] || err "No patch file to apply."
  grep -q "^diff --git " "$pf" || err "Not a unified diff: $pf"
  local sp; sp="$(safety_point)"
  say "Safety point: safety/$sp  (branch backup/$sp)"
  say "Applying patch: $pf"
  git apply --3way --whitespace=fix "$pf" || err "git apply failed (3-way). See $pf"
  git add -A
  git commit -m "[agent] apply $(basename "$pf")" || true
  git push --set-upstream origin "backup/$sp" || true
  git push --tags || true
  changed_summary
}

usage(){
  cat <<USAGE
Usage:
  tools/agent2.sh fix-ci            # fetch CI tail => ask DeepSeek => apply patch
  tools/agent2.sh do "instruction"  # freeform feature => patch => apply
  tools/agent2.sh plan "instr"      # only create patch (no apply)
  tools/agent2.sh apply [patch]     # apply latest or given patch
USAGE
}

cmd="${1:-fix-ci}"; shift || true
case "$cmd" in
  fix-ci)  pf="$(plan_ci)"; apply_patch "$pf";;
  do)      pf="$(plan_freeform "$@")"; apply_patch "$pf";;
  plan)    pf="$(plan_freeform "$@")"; echo "Patch at: $pf";;
  apply)   apply_patch "${1:-latest}";;
  *) usage; exit 2;;
esac

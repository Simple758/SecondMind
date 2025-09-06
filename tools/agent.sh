#!/usr/bin/env bash
# Minimal repo-resident agent (DeepSeek) — single file.
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

# --- config: prefer repo env, then ops, then shell ---
[[ -f "$ROOT/config/deepseek.env" ]] && . "$ROOT/config/deepseek.env" || true
[[ -f "$ROOT/ops/agent.env" ]] && . "$ROOT/ops/agent.env" || true
: "${DEEPSEEK_API_KEY:?Set DEEPSEEK_API_KEY in config/deepseek.env or ops/agent.env}"
: "${DEEPSEEK_BASE_URL:=https://api.deepseek.com}"
: "${DEEPSEEK_MODEL:=deepseek-coder}"

TS(){ date +%Y%m%d-%H%M%S; }
PATCH_DIR="$ROOT/.agent/patches"; LOG_DIR="$ROOT/.agent/logs"
mkdir -p "$PATCH_DIR" "$LOG_DIR"

say(){ printf "%s\n" "$*"; }
err(){ printf "❌ %s\n" "$*" >&2; exit 1; }

# --- helpers ---
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

api_chat(){
  # args: system_text, user_text  -> prints assistant content to stdout
  python3 - "$DEEPSEEK_BASE_URL" "$DEEPSEEK_API_KEY" "$DEEPSEEK_MODEL" <<PY "$@"
import json, sys, urllib.request
base, key, model = sys.argv[1:4]
system_text, user_text = sys.stdin.read().split("\n---USER---\n", 1)
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

extract_patch(){
  # read stdin; write a valid unified diff to stdout or exit 1
  python3 - "$@" <<PY
import sys,re
s=sys.stdin.read().strip()
# strip common fences/formatting
s=re.sub(r"^```(?:diff|patch)?\s*|\s*```$","",s,flags=re.S)
m=re.search(r"(?ms)^diff --git .*$")
if not m:
    # Sometimes models prepend prose; try to cut from first diff header
    idx=s.find("diff --git ")
    if idx==-1:
        print("No unified diff found in model output.",file=sys.stderr); sys.exit(1)
    s=s[idx:]
print(s)
PY
}

plan_ci(){
  # Build instruction from CI tail and ask for patch
  local log="$LOG_DIR/ci_tail_$(TS).log"
  tail -n 400 "$ROOT/.ops_receipts/ci_last.log" > "$log" 2>/dev/null || true
  local sys="You are a senior Android/Kotlin build fixer. Output ONLY a unified git patch (no prose, no fences). Minimal changes to make CI green. Prefer Gradle/settings/workflow edits or precise Kotlin imports/annotations. No version bumps unless error requires it."
  local usr="CI build failed. Here is the tail of the log:\n\n$(sed "s/$/\\n/" "$log")\n\nCreate ONE unified diff patch that fixes the failure."
  local out; out="$(
    { printf "%s" "$sys"; printf "\n---USER---\n"; printf "%b" "$usr"; } \
    | api_chat | extract_patch
  )" || err "Model did not return a valid patch."
  local pf="$PATCH_DIR/$(TS).patch"; printf "%s\n" "$out" > "$pf"
  echo "$pf"
}

plan_freeform(){
  # args: instruction string -> returns patch path
  local instr="$*"
  local sys="You are a precise code-editing assistant. Output ONLY a unified git patch. Keep diffs minimal and correct. No prose, no fences."
  local usr="Apply the following change to this repository:\n\n${instr}\n\nReturn ONE unified diff patch."
  local out; out="$(
    { printf "%s" "$sys"; printf "\n---USER---\n"; printf "%b" "$usr"; } \
    | api_chat | extract_patch
  )" || err "Model did not return a valid patch."
  local pf="$PATCH_DIR/$(TS).patch"; printf "%s\n" "$out" > "$pf"
  echo "$pf"
}

apply_patch(){
  # arg: patch path (or latest)
  local pf="$1"
  [[ "$pf" == "latest" ]] && pf="$(ls -t "$PATCH_DIR"/*.patch 2>/dev/null | head -n1 || true)"
  [[ -n "$pf" && -s "$pf" ]] || err "No patch file to apply."
  grep -q "^diff --git " "$pf" || err "Not a unified diff: $pf"
  local sp; sp="$(safety_point)"
  say "Safety point: safety/$sp  (branch backup/$sp)"
  say "Applying patch: $pf"
  if ! git apply --3way --whitespace=fix "$pf"; then
    err "git apply failed (3-way). See $pf"
  fi
  say "Committing & pushing…"
  git add -A
  git commit -m "[agent] apply $(basename "$pf")" || true
  git push --set-upstream origin "backup/$sp" || true
  git push --tags || true
  changed_summary
}

usage(){
  cat <<USAGE
Usage:
  tools/agent.sh fix-ci            # fetch CI tail => ask DeepSeek => apply patch
  tools/agent.sh do "instruction"  # freeform feature => patch => apply
  tools/agent.sh plan "instr"      # just create patch (no apply)
  tools/agent.sh apply [patch]     # apply latest or given patch
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

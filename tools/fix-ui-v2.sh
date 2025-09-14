#!/usr/bin/env bash
set -euo pipefail

echo "==> Safety snapshot"
git branch -f backup-pre-ui-fix-$(date +%Y%m%d-%H%M%S) >/dev/null 2>&1 || true

changed=0
mark_if_changed() {
  local f="$1"
  if ! git diff --quiet -- "$f"; then
    echo "---- BEFORE: $f"
    git --no-pager diff -- "$f" || true
    changed=1
  fi
}

edit_perl() {
  local f="$1"; shift
  [[ -f "$f" ]] || return 0
  local before_sha; before_sha="$(git hash-object "$f")"
  perl -0777 -pe "$@" -i -- "$f"
  local after_sha;  after_sha="$(git hash-object "$f")"
  [[ "$before_sha" != "$after_sha" ]] && mark_if_changed "$f"
}

echo "==> Fix #1: Remove height caps that box in scrollables"
MA=app/src/main/java/com/secondmind/minimal/MainActivity.kt
if [[ -f "$MA" ]]; then
  edit_perl "$MA" "s/\\.heightIn\\s*\\(\\s*min\\s*=\\s*[^,]+,\\s*max\\s*=\\s*[^\\)]+\\)//g"
fi

echo ==

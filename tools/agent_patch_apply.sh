#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

PATCH_ARG="${1:-latest}"
if [[ "$PATCH_ARG" == "latest" ]]; then
  PATCH="$(ls -t "$ROOT/ops/patches"/*.patch 2>/dev/null | head -n1)"
  [[ -z "${PATCH:-}" ]] && { echo "No patches found in ops/patches"; exit 2; }
else
  PATCH="$PATCH_ARG"
fi

echo "== Safety tag/branch =="
TS="$(date +%Y%m%d-%H%M%S)"
git tag -f "safety/$TS" || true
git checkout -b "backup/$TS" >/dev/null 2>&1 || git checkout "backup/$TS"

echo "== Validating patch header =="
grep -q "^diff --git " "$PATCH" || { echo "❌ Not a unified diff: $PATCH"; exit 1; }

echo "== Applying patch (3-way) =="
git apply --3way --whitespace=fix "$PATCH" || { echo "❌ git apply failed"; exit 1; }

echo "== Showing staged changes =="
git --no-pager diff

echo "== Committing =="
git add -A
git commit -m "[agent-patch] apply $(basename "$PATCH")" || true

echo "== Pushing =="
git push --set-upstream origin "backup/$TS" || true
git push --tags || true

echo "== Receipt copy =="
mkdir -p "$ROOT/ops/receipts"
cp -f "$PATCH" "$ROOT/ops/receipts/$(basename "$PATCH")"
echo "✅ Applied and pushed. Receipt stored."

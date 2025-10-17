#!/usr/bin/env bash
set -Eeuo pipefail
set +H

cd "$(git rev-parse --show-toplevel 2>/dev/null || echo "$HOME/SecondMind")"

DC="app/src/main/java/com/secondmind/minimal/ui/DrawerContent.kt"
[ -f "$DC" ] || { echo "❌ $DC not found"; exit 1; }

echo "== BEFORE (excerpt) =="
nl -ba "$DC" | sed -n '1,80p'; echo

CHANGED=0

# 1) Ensure SmartToy import (if missing)
if ! grep -qE 'import[[:space:]]+androidx\.compose\.material\.icons\.filled\.SmartToy' "$DC"; then
  echo "-- Adding SmartToy import --"
  awk 'NR==1{print; next} NR==2{print "import androidx.compose.material.icons.filled.SmartToy"; print; next} {print}' "$DC" > "$DC.__tmp__" && mv "$DC.__tmp__" "$DC"
  CHANGED=1
fi

# 2) Insert AI drawer item above Developer (idempotent)
if grep -q 'DrawerItem("AI"' "$DC"; then
  echo "-- AI item already present --"
else
  echo "-- Inserting AI drawer item above Developer --"
  awk '
    BEGIN{added=0}
    /DrawerItem\("Developer"/ && !added {
      print "    DrawerItem(\"AI\", Icons.Filled.SmartToy, \"ai\"),"
      added=1
    }
    { print }
  ' "$DC" > "$DC.__tmp__" && mv "$DC.__tmp__" "$DC"
  CHANGED=1
fi

# 3) Ensure trailing newline
tail -c1 "$DC" | od -An -t u1 | grep -q 10 || echo >> "$DC"

if [ "$CHANGED" -eq 1 ]; then
  git add "$DC"
  echo "-- Staged diff (DrawerContent.kt) --"
  git --no-pager diff --cached -- "$DC" | sed -n '1,200p'
else
  echo "No changes needed."
fi

echo
echo "== AFTER (excerpt) =="
nl -ba "$DC" | sed -n '1,80p'; echo
echo "✅ Drawer patch applied (idempotent)."

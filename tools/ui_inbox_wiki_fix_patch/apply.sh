#!/usr/bin/env bash
set -Eeuo pipefail; set +H

ROOT="$(git rev-parse --show-toplevel 2>/dev/null || echo "$PWD")"
cd "$ROOT"

ts="$(date +%Y%m%d-%H%M%S)"
backup(){ [ -f "$1" ] && cp -f "$1" "$1.bak.$ts" || :; }

MA="app/src/main/java/com/secondmind/minimal/MainActivity.kt"
WC="app/src/main/java/com/secondmind/minimal/feature/wiki/WikiCard.kt"
NR="app/src/main/java/com/secondmind/minimal/ui/NavigationRoutes.kt"

[ -f "$MA" ] || { echo "❌ $MA missing"; exit 1; }

echo "== Repo: $ROOT"

# 1) Fix MainActivity overlay (replace // InboxAIOverlay … up to first composable(\"news\"))
backup "$MA"

REPL_FILE="$(mktemp)"
cat > "$REPL_FILE" << "KTBLOCK"
// InboxAIOverlay
composable("inbox") {
  val ctx = androidx.compose.ui.platform.LocalContext.current
  androidx.compose.foundation.layout.Box(
    modifier = androidx.compose.ui.Modifier.fillMaxSize()
  ) {
    com.secondmind.minimal.ui.InboxScreen()
    androidx.compose.material3.FloatingActionButton(
      onClick = {
        com.secondmind.minimal.inbox.InboxGate.active = true
        com.secondmind.minimal.notify.SecondMindNotificationListener.triggerRebind(ctx)
      },
      modifier = androidx.compose.ui.Modifier
        .align(androidx.compose.ui.Alignment.BottomEnd)
        .padding(16.dp)
    ) {
      androidx.compose.material3.Icon(
        imageVector = androidx.compose.material.icons.Icons.Filled.SmartToy,
        contentDescription = "Summarize"
      )
    }
  }
}
KTBLOCK

TMP="$MA.__new__"
awk -v repl="$REPL_FILE" '
  BEGIN{state=0; replaced=0}
  {
    if(state==0){
      if($0 ~ /\/\/[[:space:]]*InboxAIOverlay/){
        while( (getline L < repl) > 0 ) print L;
        close(repl);
        state=1; replaced=1; next
      }
      print; next
    } else if(state==1){
      if($0 ~ /^[[:space:]]*composable\("news"\)/){ print; state=2; next }
      else next
    } else {
      print
    }
  }
  END{
    if(replaced==0){
      # no replacement happened — just output nothing special here
    }
  }
' "$MA" > "$TMP" || true

if [ -s "$TMP" ]; then
  mv "$TMP" "$MA"
  echo "✓ Rewrote Inbox overlay block in MainActivity.kt"
else
  echo "⚠️  Could not rewrite $MA (keeping original)."
fi
rm -f "$REPL_FILE"

# 2) Ensure wiki composable exists after news
if ! grep -q 'composable("wiki")' "$MA"; then
  awk '
    BEGIN{ins=0}
    {
      print
      if(!ins && $0 ~ /^[[:space:]]*composable\("news"\)/){
        print "    composable(\"wiki\") { com.secondmind.minimal.feature.wiki.WikiScreen() }"
        ins=1
      }
    }
  ' "$MA" > "$MA.__tmp__" && mv "$MA.__tmp__" "$MA"
  echo "✓ Inserted wiki composable after news"
else
  echo "• wiki composable already present (skip)"
fi

# 3) Safe WikiBrainFoodCard
backup "$WC"
mkdir -p "$(dirname "$WC")"
cat > "$WC" << "KOT"
package com.secondmind.minimal.feature.wiki

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.secondmind.minimal.ui.NavigationRoutes

@Composable
fun WikiBrainFoodCard(
    modifier: Modifier = Modifier,
    navController: NavController? = null,
    onOpen: (() -> Unit)? = null
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                when {
                    navController != null -> navController.navigate(NavigationRoutes.WIKI)
                    onOpen != null -> onOpen()
                }
            },
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Wikipedia", style = MaterialTheme.typography.titleMedium)
            Text("Ask a quick fact from Wikipedia.", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
KOT
echo "✓ Wrote safe WikiBrainFoodCard"

# 4) Ensure WIKI route constant exists
if [ -f "$NR" ]; then
  if ! grep -q 'const val WIKI *= *"wiki"' "$NR"; then
    backup "$NR"
    awk '
      BEGIN{done=0}
      {
        print
      }
      END{
        # If constant not found, append inside object block if simple form or at end as fallback
      }
    ' "$NR" > "$NR.__tmp__"

    # Simple append if inside object is hard to detect reliably; just ensure the constant line exists one way or another.
    if ! grep -q 'const val WIKI *= *"wiki"' "$NR.__tmp__"; then
      echo '    const val WIKI = "wiki"' >> "$NR.__tmp__"
    fi
    mv "$NR.__tmp__" "$NR"
    echo "✓ Ensured WIKI route constant"
  else
    echo "• WIKI route constant present (skip)"
  fi
else
  echo "⚠️  $NR not found (skipping route constant step)"
fi

# 5) Brace balance check
opens=$(grep -o "{" "$MA" | wc -l | tr -d " ")
closes=$(grep -o "}" "$MA" | wc -l | tr -d " ")
echo "MainActivity brace balance: {=$opens }=$closes"

# 6) Stage and show context
git add "$MA" "$WC" 2>/dev/null || true
[ -f "$NR" ] && git add "$NR" 2>/dev/null || true

echo
echo "== Staged files =="
git diff --cached --name-only || true

echo
echo "== Context around overlay and wiki =="
grep -n "// InboxAIOverlay" "$MA" | head -n1 | while read -r ln; do nl -ba "$MA" | sed -n "$((ln-3)),$((ln+40))p"; done || true
grep -nE "composable\\(\"news\"\\)|composable\\(\"wiki\"\\)" "$MA" | sed -n "1,120p" || true

echo
echo "✅ Fix applied & staged (no commit, no push)."

#!/usr/bin/env bash
set -Eeuo pipefail
set +H

echo "== Repo: $(pwd)"

MA="app/src/main/java/com/secondmind/minimal/MainActivity.kt"
WC="app/src/main/java/com/secondmind/minimal/feature/wiki/WikiCard.kt"
NR="app/src/main/java/com/secondmind/minimal/ui/NavigationRoutes.kt"

[ -f "$MA" ] || { echo "❌ $MA missing"; exit 1; }

# ---------- 1) Rewrite Inbox overlay block (between marker and news route) ----------
if grep -q "// InboxAIOverlay" "$MA"; then
  TMP="$MA.__new__"
  awk '
    BEGIN{state=0}
    {
      if(state==0){
        if($0 ~ /\/\/ InboxAIOverlay/){
          # print our canonical overlay once
          print "// InboxAIOverlay"
          print "    composable(\"inbox\") {"
          print "      val ctx = androidx.compose.ui.platform.LocalContext.current"
          print "      androidx.compose.foundation.layout.Box(modifier = androidx.compose.ui.Modifier.fillMaxSize()) {"
          print "        InboxScreen()"
          print "        androidx.compose.material3.FloatingActionButton("
          print "          onClick = {"
          print "            com.secondmind.minimal.inbox.InboxGate.active = true"
          print "            com.secondmind.minimal.notify.SecondMindNotificationListener.triggerRebind(ctx)"
          print "          },"
          print "          modifier = androidx.compose.ui.Modifier"
          print "            .align(androidx.compose.ui.Alignment.BottomEnd)"
          print "            .padding(16.dp)"
          print "        ) {"
          print "          androidx.compose.material3.Icon("
          print "            imageVector = androidx.compose.material.icons.Icons.Filled.SmartToy,"
          print "            contentDescription = \"AI\""
          print "          )"
          print "        }"
          print "      }"
          print "    }"
          state=1
          next
        } else {
          print $0
        }
      } else {
        # Skip everything until the next composable(\"news\")
        if($0 ~ /^[[:space:]]*composable\(\"news\"\)/){
          print $0
          state=0
        } else {
          # skip
        }
      }
    }
    END{
      if(state!=0){
        # If we never saw news, just end after printing overlay
        # (nothing to do)
        ;
      }
    }
  ' "$MA" > "$TMP"
  if cmp -s "$MA" "$TMP"; then
    rm -f "$TMP"
    echo "• Overlay block unchanged (no rewrite needed)"
  else
    mv "$TMP" "$MA"
    echo "✓ Rewrote Inbox overlay block in MainActivity.kt"
  fi
else
  echo "• Marker // InboxAIOverlay not found — skipping overlay rewrite"
fi

# ---------- 2) Ensure wiki composable exists right after news (idempotent) ----------
if grep -q 'composable("wiki")' "$MA"; then
  echo "• wiki composable already present (skip)"
else
  TMP="$MA.__new2__"
  awk '
    BEGIN{ins=0}
    {
      print $0
      if(ins==0 && $0 ~ /^[[:space:]]*composable\(\"news\"\)/){
        print "    composable(\"wiki\") { com.secondmind.minimal.feature.wiki.WikiScreen() }"
        ins=1
      }
    }
  ' "$MA" > "$TMP"
  mv "$TMP" "$MA"
  echo "✓ Inserted wiki composable after news"
fi

# ---------- 3) Safe WikiBrainFoodCard replacement (idempotent) ----------
mkdir -p "$(dirname "$WC")"
cat > "$WC.__new__" <<"KOT"
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

/**
 * Minimal, compile-clean card that navigates to the in-app Wiki screen.
 * Works with either navController or onOpen() callback.
 */
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
                    else -> { /* no-op */ }
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
if [ ! -f "$WC" ] || ! cmp -s "$WC" "$WC.__new__"; then
  mv "$WC.__new__" "$WC"
  echo "✓ Wrote safe WikiBrainFoodCard"
else
  rm -f "$WC.__new__"
  echo "• WikiBrainFoodCard already matches (skip)"
fi

# ---------- 4) Optional: ensure route constant exists ----------
if [ -f "$NR" ]; then
  if grep -q 'const val WIKI *= *"wiki"' "$NR"; then
    echo "• WIKI route constant present (skip)"
  else
    sed -i.bak."$(date +%s)" 's/^\(\s*const val DEVELOPER.*\)$/\1\n    const val WIKI = "wiki"/' "$NR" || true
    echo "✓ Ensured WIKI route constant"
  fi
fi

# ---------- 5) Brace-balance report ----------
opens=$(grep -o "{" "$MA" | wc -l | tr -d " ")
closes=$(grep -o "}" "$MA" | wc -l | tr -d " ")
echo
echo "MainActivity brace balance: {=$opens }=$closes"

echo
echo "== Staged files =="
git add "$MA" "$WC" 2>/dev/null || true
git diff --cached --name-only || true

echo
echo "== Context around overlay and wiki =="
grep -n "// InboxAIOverlay" "$MA" || true
grep -nE 'composable\("news"\)|composable\("wiki"\)' "$MA" || true

echo
echo "✅ Fix applied & staged (no commit, no push)."

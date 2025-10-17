#!/usr/bin/env bash
set -Eeuo pipefail
set +H

ROOT="$(git rev-parse --show-toplevel 2>/dev/null || echo "$PWD")"
cd "$ROOT"

echo "== Repo: $ROOT"

MA="app/src/main/java/com/secondmind/minimal/MainActivity.kt"
ROUTES="app/src/main/java/com/secondmind/minimal/ui/NavigationRoutes.kt"
WIKICARD="app/src/main/java/com/secondmind/minimal/feature/wiki/WikiCard.kt"

ts="$(date +%Y%m%d-%H%M%S)"

# ---------- Helpers ----------
backup_file() {
  local f="$1"
  [ -f "$f" ] && cp -f "$f" "$f.bak.$ts" || true
}

insert_after_line() {
  # $1=file, $2=pattern, $3=insert_text
  local f="$1"; local pat="$2"; local ins="$3"
  local ln
  ln="$(grep -nE "$pat" "$f" | head -n1 | cut -d: -f1 || true)"
  [ -n "$ln" ] || return 1
  head -n "$ln" "$f"
  printf "%s\n" "$ins"
  tail -n +"$((ln+1))" "$f"
}

splice_range_with_text() {
  # $1=file, $2=start_line (inclusive), $3=end_line (exclusive), $4=insert_text
  local f="$1"; local s="$2"; local e="$3"; local ins="$4"
  head -n "$((s-1))" "$f"
  printf "%s\n" "$ins"
  tail -n +"$e" "$f"
}

# ---------- 1) Fix Inbox overlay block ----------
if [ -f "$MA" ]; then
  backup_file "$MA"

  # Find marker or create one at first inbox route occurrence
  marker_line="$(grep -n '^.*//[[:space:]]*InboxAIOverlay' "$MA" | head -n1 | cut -d: -f1 || true)"
  if [ -z "$marker_line" ]; then
    # Try to add a marker line right before the first inbox composable, else before news
    tmp="$MA.__tmp__"
    if grep -q '^[[:space:]]*composable("inbox")' "$MA"; then
      sed -e '0,/^[[:space:]]*composable("inbox")/s//  \/\/ InboxAIOverlay\n  composable("inbox")/' "$MA" > "$tmp"
      mv "$tmp" "$MA"
    else
      # Insert a fresh overlay after home/settings if no inbox composable exists
      :
    fi
    marker_line="$(grep -n '^.*//[[:space:]]*InboxAIOverlay' "$MA" | head -n1 | cut -d: -f1 || true)"
  fi

  # Determine end anchor: prefer next "composable("news")", fallback to wiki, ai, developer, notification route
  end_line_rel="$(sed -n "$((marker_line+1)),\$p" "$MA" | grep -n '^[[:space:]]*composable("news")' | head -n1 | cut -d: -f1 || true)"
  if [ -z "$end_line_rel" ]; then
    for pat in '^[[:space:]]*composable\("wiki"\)' '^[[:space:]]*composable\("ai"\)' '^[[:space:]]*composable\("developer"\)' '^[[:space:]]*composable\("notification/' ; do
      end_line_rel="$(sed -n "$((marker_line+1)),\$p" "$MA" | grep -nE "$pat" | head -n1 | cut -d: -f1 || true)"
      [ -n "$end_line_rel" ] && break
    done
  fi
  if [ -z "$end_line_rel" ]; then
    # fallback: to end-of-file
    end_line="$(( $(wc -l < "$MA") + 1 ))"
  else
    end_line="$(( marker_line + end_line_rel ))"
  fi

  overlay_block='  // InboxAIOverlay
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
          contentDescription = "AI"
        )
      }
    }
  }'

  tmpfile="$MA.__new__"
  splice_range_with_text "$MA" "$((marker_line+1))" "$end_line" "$overlay_block" > "$tmpfile"
  mv "$tmpfile" "$MA"
  echo "✓ Rewrote Inbox overlay block in MainActivity.kt"
else
  echo "❌ $MA not found"
fi

# ---------- 2) Ensure wiki composable exists right after news ----------
if [ -f "$MA" ]; then
  if ! grep -q '^[[:space:]]*composable("wiki")' "$MA"; then
    tmpfile="$MA.__new__"
    insert_after_line "$MA" '^[[:space:]]*composable\("news"\)' '    composable("wiki") { com.secondmind.minimal.feature.wiki.WikiScreen() }' > "$tmpfile" || cp -f "$MA" "$tmpfile"
    mv "$tmpfile" "$MA"
    echo "✓ Inserted wiki composable after news"
  else
    echo "• wiki composable already present (skip)"
  fi
fi

# ---------- 3) Safe replacement for WikiBrainFoodCard ----------
mkdir -p "$(dirname "$WIKICARD")"
cat > "$WIKICARD" <<'KOT'
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
 * Minimal, safe Wiki card that navigates in-app.
 * Works with optional navController OR onOpen lambda.
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
echo "✓ Wrote safe WikiBrainFoodCard"

# ---------- 4) Ensure route constant ----------
if [ -f "$ROUTES" ]; then
  if ! grep -q 'const val WIKI *= *"wiki"' "$ROUTES"; then
    backup_file "$ROUTES"
    sed -i'' -e 's/}\s*$/    const val WIKI = "wiki"\n}\n/' "$ROUTES"
    echo "✓ Added WIKI route constant"
  else
    echo "• WIKI route constant present (skip)"
  fi
fi

# ---------- 5) Brace balance info ----------
opens=$(grep -o "{" "$MA" | wc -l | tr -d " ")
closes=$(grep -o "}" "$MA" | wc -l | tr -d " ")
echo "MainActivity brace balance: {=$opens }=$closes"

echo
echo "== Staged files =="
git add "$MA" "$WIKICARD" "$ROUTES" 2>/dev/null || true
git diff --cached --name-only || true

echo
echo "== Context around overlay and wiki =="
# Show 2 anchors: overlay marker and any wiki composable
grep -n "//[[:space:]]*InboxAIOverlay" "$MA" || true
grep -n '^[[:space:]]*composable("news")' "$MA" | head -n1 || true
grep -n '^[[:space:]]*composable("wiki")' "$MA" | head -n1 || true

echo
echo "✅ Fix applied & staged (no commit, no push)."

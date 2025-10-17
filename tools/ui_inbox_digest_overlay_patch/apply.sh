#!/usr/bin/env bash
# Idempotent patch: adds InboxOverlay.kt and wires it into the inbox route.
set -Eeuo pipefail
set +H

ROOT="$(git rev-parse --show-toplevel 2>/dev/null || echo "$HOME/SecondMind")"
cd "$ROOT" || { echo "❌ repo root not found"; exit 1; }
echo "== Repo: $(pwd)"

MAIN="app/src/main/java/com/secondmind/minimal/MainActivity.kt"
OVERLAY="app/src/main/java/com/secondmind/minimal/ui/InboxOverlay.kt"

# 1) Install/refresh the overlay file
mkdir -p "$(dirname "$OVERLAY")"
TMP_OV="$OVERLAY.__tmp__"
cat > "$TMP_OV" <<'KOT'
package com.secondmind.minimal.ui

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.secondmind.minimal.inbox.InboxGate
import com.secondmind.minimal.notify.SecondMindNotificationListener
import com.secondmind.minimal.feature.inbox.NotificationSummarizer
import com.secondmind.minimal.util.NotificationAccess

/**
 * A small overlay FAB for the Inbox route that runs a one-shot AI digest.
 * Privacy: gated, explicit, on-demand only.
 */
@Composable
fun InboxAIOverlay(padding: PaddingValues = PaddingValues(0.dp)) {
  val ctx = LocalContext.current
  val scope = rememberCoroutineScope()
  var dialogOpen by remember { mutableStateOf(false) }
  var digest by remember { mutableStateOf<String>("") }
  var busy by remember { mutableStateOf(false) }

  Box(Modifier.fillMaxSize().padding(padding)) {
    FloatingActionButton(
      onClick = {
        if (!NotificationAccess.isEnabled(ctx)) {
          // Take user to system settings; do nothing else.
          try {
            ctx.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
          } catch (_: Throwable) { /* ignore */ }
          return@FloatingActionButton
        }
        if (busy) return@FloatingActionButton
        busy = true
        InboxGate.active = true
        try {
          SecondMindNotificationListener.triggerRebind(ctx)
        } catch (_: Throwable) { }
        scope.launch {
          try {
            val result = try { NotificationSummarizer.summarizeOnce(ctx) } catch (_: Throwable) { null }
            digest = result ?: "No active notifications to summarize right now."
            dialogOpen = true
          } finally {
            InboxGate.active = false
            busy = false
          }
        }
      },
      containerColor = MaterialTheme.colorScheme.primary,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(16.dp)
    ) {
      Icon(Icons.Filled.SmartToy, contentDescription = "AI digest")
    }
  }

  if (dialogOpen) {
    AlertDialog(
      onDismissRequest = { dialogOpen = false },
      title = { Text("Inbox digest") },
      text = { Text(digest) },
      confirmButton = {
        TextButton(onClick = { dialogOpen = false }) { Text("Close") }
      }
    )
  }
}

KOT

if [ ! -f "$OVERLAY" ] || ! cmp -s "$OVERLAY" "$TMP_OV"; then
  mv "$TMP_OV" "$OVERLAY"
  git add "$OVERLAY"
  echo "• Installed/updated:  $OVERLAY"
else
  rm -f "$TMP_OV"
  echo "• Unchanged:          $OVERLAY"
fi

# 2) Wire the overlay into the inbox route in MainActivity.kt
if [ ! -f "$MAIN" ]; then
  echo "❌ $MAIN not found"
  exit 1
fi

if grep -q 'InboxAIOverlay' "$MAIN"; then
  echo "• MainActivity already references InboxAIOverlay (skipping edit)"
else
  # first try: replace a simple one-liner form
  if grep -q 'composable("inbox")[^\n]*{[[:space:]]*InboxScreen()[[:space:]]*}' "$MAIN"; then
    # Use sed to expand the one-liner into a block with the overlay
    sed -E -i 's#composable\("inbox"\)[[:space:]]*\{[[:space:]]*InboxScreen\(\)[[:space:]]*\}#composable("inbox") {\
  InboxScreen()\
  // InboxAIOverlay\
  com.secondmind.minimal.ui.InboxAIOverlay()\
}#g' "$MAIN"
    echo "• Rewrote one-line inbox route to include overlay."
  else
    # fallback: insert overlay immediately after first InboxScreen() that appears
    # after the first appearance of composable("inbox")
    TMP_MAIN="$MAIN.__tmp__"
    awk '
      BEGIN{seen=0; inserted=0}
      /composable\("inbox"\)/ { seen=1 }
      {
        print
        if(seen==1 && inserted==0 && $0 ~ /InboxScreen\(\)/){
          print "      // InboxAIOverlay"
          print "      com.secondmind.minimal.ui.InboxAIOverlay()"
          inserted=1
          seen=0
        }
      }
    ' "$MAIN" > "$TMP_MAIN"
    if cmp -s "$MAIN" "$TMP_MAIN"; then
      rm -f "$TMP_MAIN"
      echo "⚠️  Could not automatically insert overlay (pattern not found)."
      echo "    Please add inside inbox composable:  com.secondmind.minimal.ui.InboxAIOverlay()"
    else
      mv "$TMP_MAIN" "$MAIN"
      echo "• Inserted overlay after InboxScreen() inside inbox route."
    fi
  fi
  git add "$MAIN"
fi

echo
echo "== Staged files =="
git diff --name-only --cached || true

echo
echo "== Preview (MainActivity inbox excerpt) =="
grep -n 'composable("inbox")' -n "$MAIN" | head -n1 | cut -d: -f1 | {
  read ln || ln=1
  nl -ba "$MAIN" | sed -n "$((ln)), $((ln+40))p"
}
echo
echo "✅ Inbox digest overlay patched (no commit, no push)."

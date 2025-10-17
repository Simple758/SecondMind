    #!/usr/bin/env bash
    set -Eeuo pipefail
    set +H
    cd "$(git rev-parse --show-toplevel 2>/dev/null || echo "$HOME/SecondMind")"
    echo "== Repo: $(pwd)"

    LISTENER="app/src/main/java/com/secondmind/minimal/notify/SecondMindNotificationListener.kt"
    MA="app/src/main/java/com/secondmind/minimal/MainActivity.kt"

    # ---------- 1) Gate the notification listener (full replace, idempotent via marker) ----------
    if [ -f "$LISTENER" ]; then
      if grep -q "-- GATED --" "$LISTENER"; then
        echo "Listener already gated."
      else
        echo "Gating listener..."
        cp -f "$LISTENER" "$LISTENER.bak"
        cat > "$LISTENER" <<'KOT'
    package com.secondmind.minimal.notify

import android.content.ComponentName
import android.content.Context
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.secondmind.minimal.inbox.InboxGate
import com.secondmind.minimal.inbox.InboxStore

class SecondMindNotificationListener : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()
        if (!InboxGate.active) return
        Log.d("SM-Notif", "listener connected (gated)")
        try {
            val pm = applicationContext.packageManager
            val list = activeNotifications ?: emptyArray()
            for (sbn in list) {
                val label = try {
                    pm.getApplicationLabel(pm.getApplicationInfo(sbn.packageName, 0)).toString()
                } catch (_: Throwable) { null }
                InboxStore.push(sbn, label)
            }
        } catch (_: Throwable) { /* ignore */ }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (!InboxGate.active) return
        try {
            val pm = applicationContext.packageManager
            val label = try {
                pm.getApplicationLabel(pm.getApplicationInfo(sbn.packageName, 0)).toString()
            } catch (_: Throwable) { null }
            InboxStore.push(sbn, label)
        } catch (_: Throwable) { /* ignore */ }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        try { InboxStore.remove(sbn.key) } catch (_: Throwable) { /* ignore */ }
    }

    companion object {
        // -- GATED -- marker for idempotent patch
        fun triggerRebind(ctx: Context) {
            try {
                requestRebind(ComponentName(ctx, SecondMindNotificationListener::class.java))
            } catch (_: Throwable) { }
        }
    }
}

KOT
        git add "$LISTENER"
      fi
    else
      echo "❌ Listener file not found: $LISTENER"
    fi

    # ---------- 2) Ensure imports for FAB & icon in MainActivity ----------
    if [ -f "$MA" ]; then
      ensure_import() {
        local imp="$1"
        grep -qE "^import[[:space:]]+$imp" "$MA" || sed -i "1,/^package/s|^package .*|&\nimport $imp|" "$MA"
      }
      ensure_import "androidx.compose.material3.FloatingActionButton"
      ensure_import "androidx.compose.material.icons.filled.SmartToy"
      ensure_import "androidx.compose.ui.Alignment"
      ensure_import "androidx.compose.ui.unit.dp"
      ensure_import "androidx.compose.ui.platform.LocalContext"
      ensure_import "androidx.compose.material.icons.Icons"
      ensure_import "androidx.compose.material3.Icon"
      ensure_import "androidx.compose.foundation.layout.Box"
      ensure_import "androidx.compose.foundation.layout.padding"

      # ---------- 3) Replace single-line inbox composable with overlay (idempotent) ----------
      if grep -q "InboxAIOverlay" "$MA"; then
        echo "Inbox overlay already present."
      else
        echo "Patching inbox route overlay..."
        TMP="$MA.__tmp__"
        awk '
          BEGIN{replaced=0}
          # exact single-line form
          /^[[:space:]]*composable\("inbox"\)[[:space:]]*\{[[:space:]]*InboxScreen\(\)[[:space:]]*\}[[:space:]]*$/ && !replaced {
            print "  // InboxAIOverlay"
            print "  composable("inbox") {"
            print "    val ctx = androidx.compose.ui.platform.LocalContext.current"
            print "    androidx.compose.foundation.layout.Box(modifier = androidx.compose.ui.Modifier.fillMaxSize()) {"
            print "      InboxScreen()"
            print "      androidx.compose.material3.FloatingActionButton("
            print "        onClick = {"
            print "          com.secondmind.minimal.inbox.InboxGate.active = true"
            print "          com.secondmind.minimal.notify.SecondMindNotificationListener.triggerRebind(ctx)"
            print "        },"
            print "        modifier = androidx.compose.ui.Modifier.align(androidx.compose.ui.Alignment.BottomEnd).padding(16.dp)"
            print "      ) {"
            print "        androidx.compose.material3.Icon(imageVector = androidx.compose.material.icons.Icons.Filled.SmartToy, contentDescription = "Summarize")"
            print "      }"
            print "    }"
            print "  }"
            replaced=1
            next
          }
          { print }
        ' "$MA" > "$TMP" && mv "$TMP" "$MA"
        git add "$MA"
      fi
    else
      echo "❌ MainActivity not found: $MA"
    fi

    echo
    echo "== Staged files =="
    git diff --cached --name-only || true

    echo
    echo "== Preview (Listener tail) =="
    nl -ba "$LISTENER" | sed -n "1,120p" | sed -n "    1,200p"

    echo
    echo "== Preview (MainActivity inbox route lines) =="
    nl -ba "$MA" | sed -n "1,280p" | grep -n -E "InboxAIOverlay|composable\("inbox"\)" -n || true

    echo
    echo "✅ Repair patch applied (no commit, no push)."

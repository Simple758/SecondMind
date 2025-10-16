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
            val lines = com.secondmind.minimal.inbox.InboxStore.items.value.map { "${it.appLabel}: ${it.title} ${it.text}" }
            val result = try { NotificationSummarizer.summarizeWithAI(ctx, lines) } catch (_: Throwable) { null }
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


package com.secondmind.minimal.ui

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.secondmind.minimal.inbox.InboxStore
import com.secondmind.minimal.inbox.NotificationItem
import com.secondmind.minimal.tts.TtsHelper

private data class AppGroup(
    val pkg: String,
    val appLabel: String,
    val messages: List<NotificationItem>
)

@Composable
fun InboxScreen() {
    val ctx = LocalContext.current
    val items by InboxStore.items.collectAsState()

    // TTS with safe lifecycle
    val tts = remember(ctx) { TtsHelper(ctx) }
    DisposableEffect(Unit) { onDispose { tts.shutdown() } }

    // Build groups: latest on top
    val groups by remember(items) {
        mutableStateOf(
            items.groupBy { it.pkg }
                .map { (pkg, list) ->
                    val sorted = list.sortedByDescending { it.ts }
                    AppGroup(pkg, sorted.firstOrNull()?.appLabel ?: pkg, sorted)
                }
                .sortedByDescending { it.messages.firstOrNull()?.ts ?: 0L }
        )
    }

    fun speakGroup(g: AppGroup) {
        val text = buildString {
            append(g.appLabel).append(". ")
            g.messages.take(5).forEachIndexed { i, m ->
                if (m.title.isNotBlank()) append(m.title).append(". ")
                if (m.text.isNotBlank()) append(m.text)
                if (i < g.messages.lastIndex) append(". ")
            }
        }
        tts.speak(text)
    }

    fun openApp(pkg: String) {
        try {
            val intent = ctx.packageManager.getLaunchIntentForPackage(pkg)
            if (intent != null) {
                ctx.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        } catch (_: Throwable) { /* ignore */ }
    }

    Column(Modifier.fillMaxSize()) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Inbox", style = MaterialTheme.typography.headlineMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = {
                    // Speak a short digest: last message from each of the top 5 apps
                    val digest = buildString {
                        groups.take(5).forEachIndexed { gi, g ->
                            append(g.appLabel).append(". ")
                            g.messages.firstOrNull()?.let { m ->
                                if (m.title.isNotBlank()) append(m.title).append(". ")
                                if (m.text.isNotBlank()) append(m.text)
                            }
                            if (gi < groups.lastIndex) append(". ")
                        }
                    }
                    tts.speak(digest)
                }) { Text("Speak All") }
                TextButton(onClick = { InboxStore.clear() }) { Text("Clear") }
            }
        }

        if (groups.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No notifications yet", style = MaterialTheme.typography.bodyLarge)
            }
            return
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(groups) { g ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.fillMaxWidth().padding(14.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("${g.appLabel}  •  ${g.messages.size}", style = MaterialTheme.typography.titleMedium)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                TextButton(onClick = { speakGroup(g) }) { Text("Speak") }
                                TextButton(onClick = { openApp(g.pkg) }) { Text("Open") }
                            }
                        }
                        val preview = g.messages.firstOrNull()?.let { m ->
                            buildString {
                                if (m.title.isNotBlank()) append(m.title).append(" — ")
                                append(m.text.ifBlank { "(no text)" })
                            }
                        }.orEmpty()
                        if (preview.isNotBlank()) {
                            Spacer(Modifier.height(6.dp))
                            Text(preview, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

/** Compatibility overload so older call sites with params still compile. */
@Composable
fun InboxScreen(vararg unused: Any?) = InboxScreen()

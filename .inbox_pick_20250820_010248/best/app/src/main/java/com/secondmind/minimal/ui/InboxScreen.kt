package com.secondmind.minimal.ui

import android.content.Intent
import android.widget.Toast
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
import com.secondmind.minimal.tts.TtsSpeaker
import java.util.*

private data class AppGroup(
    val pkg: String,
    val label: String,
    val count: Int,
    val summary: String,
    val latestTs: Long
)

@Composable
fun InboxScreen() {
    val ctx = LocalContext.current
    val items by InboxStore.items.collectAsState()

    // Group notifications by package and sort by most recent
    val groups by remember(items) {
        mutableStateOf(
            items.groupBy { it.pkg }.map { (pkg, list) ->
                val latest = list.maxBy { it.ts }
                val summary = buildString {
                    if (latest.title.isNotBlank()) append(latest.title).append(" — ")
                    append(latest.text.ifBlank { "(no text)" })
                }
                AppGroup(
                    pkg = pkg,
                    label = latest.appLabel.ifBlank { pkg },
                    count = list.size,
                    summary = summary,
                    latestTs = latest.ts
                )
            }.sortedByDescending { it.latestTs }
        )
    }

    Column(Modifier.fillMaxSize().padding(top = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Inbox", style = MaterialTheme.typography.headlineLarge)
            TextButton(onClick = { InboxStore.clear() }) { Text("Clear") }
        }

        if (groups.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No notifications yet", style = MaterialTheme.typography.bodyLarge)
            }
            return
        }

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 12.dp)
        ) {
            items(groups) { g ->
                Card(Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
                    Column(Modifier.fillMaxWidth().padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(g.label, style = MaterialTheme.typography.titleMedium)
                                Spacer(Modifier.width(8.dp))
                                Text("• ${g.count}", style = MaterialTheme.typography.labelMedium)
                            }
                            Row {
                                TextButton(onClick = {
                                    val text = "${g.label}: ${g.summary}"
                                    TtsSpeaker.speak(ctx, text)
                                }) { Text("Speak") }

                                Spacer(Modifier.width(8.dp))

                                TextButton(onClick = {
                                    val intent = ctx.packageManager.getLaunchIntentForPackage(g.pkg)
                                    if (intent != null) {
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        ctx.startActivity(intent)
                                    } else {
                                        Toast.makeText(ctx, "Can't open ${g.label}", Toast.LENGTH_SHORT).show()
                                    }
                                }) { Text("Open") }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(g.summary, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        // Speak All
        Box(Modifier.fillMaxWidth().padding(16.dp)) {
            Button(
                onClick = {
                    // Speak top N summaries in chronological order (newest first)
                    val say = groups.take(10).joinToString(". ") { "${it.label}: ${it.summary}" }
                    if (say.isNotBlank()) TtsSpeaker.speak(ctx, say)
                },
                modifier = Modifier.align(Alignment.Center)
            ) { Text("Speak All") }
        }
    }
}

/** Compatibility overload: compiles even if old callers pass params. */
@Composable
fun InboxScreen(vararg unused: Any?) = InboxScreen()

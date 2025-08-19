package com.secondmind.minimal.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.secondmind.minimal.inbox.InboxStore
import com.secondmind.minimal.util.TtsSpeaker
import com.secondmind.minimal.util.openApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxScreen() {
    val ctx = LocalContext.current
    val items by InboxStore.items.collectAsState()

    val speaker = remember(ctx) { TtsSpeaker(ctx) }
    DisposableEffect(Unit) { onDispose { speaker.shutdown() } }

    fun copyRow(title: String, text: String) {
        val clip = (ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
        val str = buildString {
            if (title.isNotBlank()) append(title).append("\n")
            append(text)
        }.trim()
        clip.setPrimaryClip(ClipData.newPlainText("notification", str))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inbox (${items.size})") },
                actions = {
                    TextButton(onClick = {
                        val summary = buildString {
                            items.take(10).forEachIndexed { i, n ->
                                if (n.title.isNotBlank()) append(n.title).append(". ")
                                append(n.text)
                                if (i < items.lastIndex) append(". ")
                            }
                        }
                        speaker.speak(summary)
                    }) { Text("Speak") }
                    TextButton(onClick = { InboxStore.clear() }) { Text("Clear") }
                }
            )
        }
    ) { pad ->
        if (items.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(pad), contentAlignment = Alignment.Center) {
                Text("No notifications yet")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(pad)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items) { n ->
                    ListItem(
                        headlineContent = { Text(if (n.title.isNotBlank()) n.title else n.pkg) },
                        supportingContent = { if (n.text.isNotBlank()) Text(n.text) },
                        trailingContent = {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                TextButton(onClick = { openApp(ctx, n.pkg) }) { Text("Open") }
                                TextButton(onClick = { copyRow(n.title, n.text) }) { Text("Copy") }
                            }
                        }
                    )
                    Divider()
                }
            }
        }
    }
}

package com.secondmind.minimal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.secondmind.minimal.inbox.InboxStore
import com.secondmind.minimal.inbox.NotificationItem

@Composable
fun InboxScreen() {
    val items by InboxStore.items.collectAsState()
    Column(Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Inbox", style = MaterialTheme.typography.headlineMedium)
            TextButton(onClick = { InboxStore.clear() }) { Text("Clear") }
        }
        if (items.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No notifications yet", style = MaterialTheme.typography.bodyLarge)
            }
            return
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items) { n ->
                InboxCard(n)
            }
        }
    }
}

@Composable
private fun InboxCard(n: NotificationItem) {
    Card(Modifier.fillMaxWidth()) {
        Column(
            Modifier.fillMaxWidth().padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(n.appLabel, style = MaterialTheme.typography.titleMedium)
            val line = buildString {
                if (n.title.isNotBlank()) append(n.title).append(" — ")
                append(n.text)
            }.ifBlank { "(no text)" }
            Text(line, style = MaterialTheme.typography.bodyMedium, maxLines = 3, overflow = TextOverflow.Ellipsis)
        }
    }
}

/** Compatibility overload so old callers compile even if they still pass args. */
@Composable
fun InboxScreen(vararg unused: Any?) = InboxScreen()

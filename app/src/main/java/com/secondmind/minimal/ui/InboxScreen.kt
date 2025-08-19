package com.secondmind.minimal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.secondmind.minimal.inbox.InboxStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxScreen()xScreen() {
    val items by InboxStore.items.collectAsState()

old(
        topBar = {
            TopAppBar(
                title = { Text("Inbox (${items.size})") },
                actions = {
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
                        supportingContent = { if (n.text.isNotBlank()) Text(n.text) }
                    )
                    Divider()
                }
            }
        }
    }
}

package com.secondmind.minimal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class AppMessage(val id: Long, val title: String, val text: String, val ts: Long)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetailScreen(
  appLabel: String,
  messages: List<AppMessage>,
  onReadOne: (Long) -> Unit,
  onReadAll: () -> Unit,
  onBack: () -> Unit
) {
  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = { Text(appLabel) },
        navigationIcon = { TextButton(onClick = onBack) { Text("Back") } },
        actions = {
          IconButton(onClick = onReadAll) {
            Icon(Icons.Filled.VolumeUp, contentDescription = "Read all")
          }
        }
      )
    }
  ) { pad ->
    LazyColumn(
      modifier = Modifier.fillMaxSize().padding(pad).padding(12.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(messages.size) { i ->
        val m = messages[i]
        Card {
          Row(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(Modifier.weight(1f)) {
              if (m.title.isNotBlank()) Text(m.title, style = MaterialTheme.typography.titleSmall)
              Text(m.text, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = { onReadOne(m.id) }) {
              Icon(Icons.Filled.VolumeUp, contentDescription = "Read")
            }
          }
        }
      }
    }
  }
}

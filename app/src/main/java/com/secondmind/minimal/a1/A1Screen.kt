package com.secondmind.minimal.a1

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun A1Screen(
  groups: List<A1Group>,
  onOpenApp: (String) -> Unit,
  onReadApp: (A1Group) -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize().padding(12.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    items(groups.size) { i ->
      val g = groups[i]
      Card(Modifier.fillMaxWidth().clickable { onOpenApp(g.pkg) }) {
        Row(
          Modifier.fillMaxWidth().padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(Modifier.weight(1f)) {
            Text(g.appLabel, style = MaterialTheme.typography.titleMedium)
            Text("${g.messages.size} notifications", style = MaterialTheme.typography.bodySmall)
            val preview = g.messages.firstOrNull()?.let { m ->
              (if (m.title.isNotBlank()) "${m.title} — " else "") + m.text
            }.orEmpty()
            if (preview.isNotBlank()) {
              Text(preview, style = MaterialTheme.typography.labelSmall, maxLines = 1)
            }
          }
          IconButton(onClick = { onReadApp(g) }) {
            Icon(Icons.Rounded.VolumeUp, contentDescription = "Read all")
          }
        }
      }
    }
  }
}

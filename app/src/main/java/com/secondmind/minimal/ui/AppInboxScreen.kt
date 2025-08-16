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

data class AppSummary(val appPackage: String, val appLabel: String, val count: Int, val preview: String)

@Composable
fun AppInboxScreen(
  apps: List<AppSummary>,
  onOpenApp: (String) -> Unit,
  onReadApp: (String) -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize().padding(12.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    items(apps.size) { i ->
      val a = apps[i]
      Card(onClick = { onOpenApp(a.appPackage) }) {
        Row(
          Modifier.fillMaxWidth().padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(Modifier.weight(1f)) {
            Text(a.appLabel, style = MaterialTheme.typography.titleMedium)
            Text("${a.count} notifications", style = MaterialTheme.typography.bodySmall)
            if (a.preview.isNotBlank())
              Text(a.preview, style = MaterialTheme.typography.labelSmall, maxLines = 1)
          }
          IconButton(
            onClick = { onReadApp(a.appPackage) },
            shape = MaterialTheme.shapes.extraLarge
          ) { Icon(Icons.Filled.VolumeUp, contentDescription = "Read all") }
        }
      }
    }
  }
}

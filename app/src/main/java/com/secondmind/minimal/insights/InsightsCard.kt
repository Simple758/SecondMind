package com.secondmind.minimal.insights
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
@Composable
fun InsightsCard(modifier: Modifier = Modifier) {
  val ctx = LocalContext.current
  val themes by remember { mutableStateOf(InsightEngine.themes(ctx, 3)) }
  if (themes.isNotEmpty()) {
    Card(modifier.fillMaxWidth()) {
      Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Themes today", style = MaterialTheme.typography.titleMedium)
        themes.forEach { AssistChip(onClick = {}, label = { Text("${it.label} (${it.count})") }) }
      }
    }
  }
}

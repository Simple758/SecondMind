package com.secondmind.minimal.memory
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
@Composable
fun ResumeBanner(modifier: Modifier = Modifier, onOpenNews: () -> Unit = {}) {
  val ctx = LocalContext.current
  var text by remember { mutableStateOf<String?>(null) }
  LaunchedEffect(Unit) { text = MemoryStore.resumeText(ctx) }
  if (text != null) {
    Card(modifier.fillMaxWidth()) {
      Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(Modifier.weight(1f)) {
          Text("Since last time", style = MaterialTheme.typography.titleMedium)
          Spacer(Modifier.height(4.dp))
          Text(text!!, style = MaterialTheme.typography.bodyMedium)
        }
        TextButton(onClick = onOpenNews) { Text("Open News") }
      }
    }
  }
}

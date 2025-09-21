package com.secondmind.minimal.knowledge
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
@Composable
fun KnowledgeCard(modifier: Modifier = Modifier) {
  val ctx = LocalContext.current
  var open by remember { mutableStateOf(false) }
  val items by remember { mutableStateOf(KnowledgeRepo.last(ctx, 3)) }
  Card(modifier.fillMaxWidth()) {
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Text("Knowledge Base", style = MaterialTheme.typography.titleMedium)
      if (items.isEmpty()) {
        Text("Save ideas or snippets. They will show up here.")
      } else {
        items.forEach { Text("• " + it.title, style = MaterialTheme.typography.bodyMedium) }
      }
      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        TextButton(onClick = { open = true }) { Text("Add") }
      }
    }
  }
  if (open) {
    var text by remember { mutableStateOf("") }
    AlertDialog(
      onDismissRequest = { open = false },
      confirmButton = {
        TextButton(onClick = {
          if (text.isNotBlank()) KnowledgeRepo.addText(ctx, text)
          open = false
        }) { Text("Save") }
      },
      dismissButton = { TextButton(onClick = { open = false }) { Text("Cancel") } },
      title = { Text("Add to Knowledge") },
      text = { OutlinedTextField(value = text, onValueChange = { text = it }, singleLine = false, placeholder = { Text("Paste or type text...") }) }
    )
  }
}

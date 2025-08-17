package com.secondmind.minimal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.secondmind.minimal.NotesStore

@Composable
fun QuickNoteCard(modifier: Modifier = Modifier) {
  var text by remember { mutableStateOf("") }
  Card(modifier) {
    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Text("Notes", style = MaterialTheme.typography.titleMedium)
      OutlinedTextField(value = text, onValueChange = { text = it }, placeholder = { Text("Quick note…") })
      Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(enabled = text.isNotBlank(), onClick = { NotesStore.add(text); text = "" }) { Text("Add") }
        Text("Total: ${NotesStore.count()}", style = MaterialTheme.typography.labelSmall)
      }
    }
  }
}

package com.secondmind.minimal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun QuickNoteCard(modifier: Modifier = Modifier) {
  var notes by rememberSaveable { mutableStateOf(listOf<String>()) }
  val count = notes.size
  val recent = notes.lastOrNull() ?: "Quick note…"
  var show by remember { mutableStateOf(false) }

  Card(
    modifier = modifier,
    shape = MaterialTheme.shapes.large,
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Row(
      Modifier.fillMaxWidth().padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(Modifier.weight(1f)) {
        Text("Notes", style = MaterialTheme.typography.titleMedium)
        Text(
          recent, maxLines = 1, overflow = TextOverflow.Ellipsis,
          style = MaterialTheme.typography.bodySmall
        )
        Text("Total: $count", style = MaterialTheme.typography.labelSmall)
      }
      FilledTonalButton(onClick = { show = true }, shape = MaterialTheme.shapes.extraLarge) {
        Text("New")
      }
    }
  }

  if (show) {
    var text by remember { mutableStateOf("") }
    AlertDialog(
      onDismissRequest = { show = false },
      title = { Text("New Note") },
      text = {
        OutlinedTextField(
          value = text, onValueChange = { text = it },
          placeholder = { Text("Type and save") },
          modifier = Modifier.fillMaxWidth(), minLines = 3
        )
      },
      confirmButton = {
        TextButton(onClick = {
          val t = text.trim()
          if (t.isNotEmpty()) notes = notes + t
          show = false
        }) { Text("Save") }
      },
      dismissButton = { TextButton(onClick = { show = false }) { Text("Cancel") } }
    )
  }
}

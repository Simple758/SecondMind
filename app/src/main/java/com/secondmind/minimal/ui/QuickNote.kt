package com.secondmind.minimal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

private data class Note(val id: Long, val text: String, val ts: Long)

private object NoteStore {
  private var seq = 1L
  val notes = mutableStateListOf<Note>()
  fun add(text: String) { notes += Note(seq++, text.trim(), System.currentTimeMillis()) }
  fun count(): Int = notes.size
  fun latestPreview(): String = notes.lastOrNull()?.text ?: "Quick note…"
}

@Composable
fun QuickNoteCard(modifier: Modifier = Modifier) {
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
          NoteStore.latestPreview(),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          style = MaterialTheme.typography.bodySmall
        )
        Text("Total: ${NoteStore.count()}", style = MaterialTheme.typography.labelSmall)
      }
      FilledIconButton(onClick = { show = true }) {
        Icon(Icons.Rounded.Add, contentDescription = "New note")
      }
    }
  }
  if (show) QuickNoteDialog(
    onDismiss = { show = false },
    onSave = { t -> if (t.isNotBlank()) NoteStore.add(t); show = false }
  )
}

@Composable
private fun QuickNoteDialog(onDismiss: () -> Unit, onSave: (String) -> Unit) {
  var text by remember { mutableStateOf("") }
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("New Note") },
    text = {
      OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        placeholder = { Text("Type and save") },
        modifier = Modifier.fillMaxWidth(),
        minLines = 3
      )
    },
    confirmButton = { TextButton(onClick = { onSave(text) }) { Text("Save") } },
    dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
  )
}

package com.secondmind.minimal.ui
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.secondmind.minimal.notes.AppDb
import com.secondmind.minimal.notes.NoteEntity
import kotlinx.coroutines.launch
@Composable
fun QuickNoteCard(modifier: Modifier = Modifier) {
  val ctx = LocalContext.current
  val dao = remember { AppDb.get(ctx).noteDao() }
  val scope = rememberCoroutineScope()
  val count by dao.count().collectAsState(initial = 0)
  val recent by dao.recent(1).collectAsState(initial = emptyList())
  var show by remember { mutableStateOf(false) }
  Card(modifier = modifier, shape = MaterialTheme.shapes.large, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
    Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
      Column(Modifier.weight(1f)) {
        Text("Notes", style = MaterialTheme.typography.titleMedium)
        val prev = recent.firstOrNull()?.text ?: "Quick note…"
        Text(prev, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodySmall)
        Text("Total: $count", style = MaterialTheme.typography.labelSmall)
      }
      FilledIconButton(onClick = { show = true }, shape = MaterialTheme.shapes.extraLarge) { Icon(Icons.Rounded.Add, contentDescription = "New note") }
    }
  }
  if (show) QuickNoteDialog(onDismiss = { show = false }, onSave = { text ->
    if (text.isNotBlank()) scope.launch { dao.insert(NoteEntity(text = text.trim())) }
    show = false
  })
}
@Composable
private fun QuickNoteDialog(onDismiss: () -> Unit, onSave: (String) -> Unit) {
  var text by remember { mutableStateOf("") }
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("New Note") },
    text = { OutlinedTextField(value = text, onValueChange = { text = it }, placeholder = { Text("Type and save") }, modifier = Modifier.fillMaxWidth(), minLines = 3) },
    confirmButton = { TextButton(onClick = { onSave(text) }) { Text("Save") } },
    dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
  )
}

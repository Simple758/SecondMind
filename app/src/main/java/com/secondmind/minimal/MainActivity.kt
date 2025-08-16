package com.secondmind.minimal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent { SecondMindApp() }
  }
}

@Composable
fun SecondMindApp() {
  MaterialTheme {
    Surface(Modifier.fillMaxSize()) {
      HomeScreen()
    }
  }
}

@Composable
fun HomeScreen() {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Text("SecondMind", style = MaterialTheme.typography.headlineMedium)

    // H1-style top cards row: Notes (left) + Inbox (right)
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(modifier = Modifier.weight(1f)) {
        QuickNoteCard(modifier = Modifier.fillMaxWidth())
      }
      Box(modifier = Modifier.weight(1f)) {
        InboxCard(modifier = Modifier.fillMaxWidth())
      }
    }

    // Spacer for future content
    Spacer(Modifier.height(12.dp))
    Text(
      "Welcome! Core flavor is running minimal UI so builds are reliable. " +
      "We’ll wire advanced features in the full flavor.",
      style = MaterialTheme.typography.bodySmall
    )
  }
}

@Composable
fun QuickNoteCard(modifier: Modifier = Modifier) {
  // Simple in-memory notes just to compile/run; survives recomposition but not process death.
  var show by remember { mutableStateOf(false) }
  var last by remember { mutableStateOf<String?>(null) }
  var count by remember { mutableStateOf(0) }

  Card(modifier = modifier, shape = MaterialTheme.shapes.large, elevation = CardDefaults.cardElevation(2.dp)) {
    Row(
      Modifier.fillMaxWidth().padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(Modifier.weight(1f)) {
        Text("Notes", style = MaterialTheme.typography.titleMedium)
        Text(
          last ?: "Quick note…",
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
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
          value = text,
          onValueChange = { text = it },
          placeholder = { Text("Type and save") },
          modifier = Modifier.fillMaxWidth(),
          minLines = 3
        )
      },
      confirmButton = {
        TextButton(onClick = {
          val t = text.trim()
          if (t.isNotEmpty()) {
            last = t
            count += 1
          }
          show = false
        }) { Text("Save") }
      },
      dismissButton = { TextButton(onClick = { show = false }) { Text("Cancel") } }
    )
  }
}

@Composable
fun InboxCard(modifier: Modifier = Modifier) {
  Card(modifier = modifier, shape = MaterialTheme.shapes.large, elevation = CardDefaults.cardElevation(2.dp)) {
    Column(
      Modifier.fillMaxWidth().padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      horizontalAlignment = Alignment.Start
    ) {
      Text("Inbox", style = MaterialTheme.typography.titleMedium)
      Text("Your captured notifications", style = MaterialTheme.typography.bodySmall)
      FilledTonalButton(
        onClick = { /* no-op in core flavor; full flavor will navigate */ },
        shape = MaterialTheme.shapes.extraLarge
      ) { Text("Open Inbox") }
    }
  }
}

package com.secondmind.minimal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.OutlinedTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent { App() }
  }
}

@Composable
fun App() {
  MaterialTheme {
    Surface(modifier = Modifier.fillMaxSize()) {
      HomeScreen()
    }
  }
}

@Composable
fun HomeScreen() {
  Column(
    Modifier.fillMaxSize().padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Text("SecondMind", style = MaterialTheme.typography.headlineSmall)

    Row(
      Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      verticalAlignment = Alignment.Top
    ) {
      Box(Modifier.weight(1f)) { QuickNoteCard() }
      Box(Modifier.weight(1f)) { InboxCard() }
    }
  }
}

@Composable
fun QuickNoteCard() {
  var text by remember { mutableStateOf("") }
  var notes by remember { mutableStateOf(listOf<String>()) }

  Card(
    shape = MaterialTheme.shapes.large,
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      Modifier.fillMaxWidth().padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Text("Notes", style = MaterialTheme.typography.titleMedium)

      val preview = notes.lastOrNull() ?: "Quick note…"
      Text(
        preview,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.bodySmall
      )

      OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        placeholder = { Text("Type note") },
        modifier = Modifier.fillMaxWidth()
      )

      Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Button(onClick = {
          if (text.isNotBlank()) {
            notes = notes + text.trim()
            text = ""
          }
        }) { Text("Save") }

        Text("Total: ${notes.size}", style = MaterialTheme.typography.labelSmall)
      }
    }
  }
}

@Composable
fun InboxCard(onOpen: (() -> Unit)? = null) {
  Card(
    shape = MaterialTheme.shapes.large,
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      Modifier.fillMaxWidth().padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Text("Inbox", style = MaterialTheme.typography.titleMedium)
      Text("Your captured notifications", style = MaterialTheme.typography.bodySmall)
      FilledTonalButton(onClick = { onOpen?.invoke() }) {
        Text("Open Inbox")
      }
    }
  }
}

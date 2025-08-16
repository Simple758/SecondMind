package com.secondmind.minimal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // Ensure a flavor init class is loaded (core/full define FlavorInit)
    try { Class.forName("com.secondmind.minimal.FlavorInit") } catch (_: Throwable) {}
    setContent { App() }
  }
}

@Composable
fun App() {
  val nav = rememberNavController()
  MaterialTheme {
    NavHost(navController = nav, startDestination = "home") {
      composable("home") { HomeScreen(onOpenInbox = { nav.navigate("inbox") }) }
      composable("inbox") { InboxScreen(onBack = { nav.popBackStack() }) }
    }
  }
}

@Composable
fun HomeScreen(onOpenInbox: () -> Unit) {
  val scope = rememberCoroutineScope()
  val notesRepo = Providers.notes
  var count by remember { mutableStateOf(0) }
  var recent by remember { mutableStateOf<List<Note>>(emptyList()) }

  LaunchedEffect(Unit) {
    count = notesRepo.count()
    recent = notesRepo.list(1)
  }

  Column(Modifier.fillMaxSize().padding(16.dp),
         verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Text("SecondMind", style = MaterialTheme.typography.headlineSmall)

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      // Notes card (H1 left)
      Card(modifier = Modifier.weight(1f)) {
        Column(Modifier.fillMaxWidth().padding(16.dp),
               verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text("Notes", style = MaterialTheme.typography.titleMedium)
          Text(
            recent.firstOrNull()?.text ?: "Quick note…",
            maxLines = 1, overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodySmall
          )
          Text("Total: $count", style = MaterialTheme.typography.labelSmall)
          var show by remember { mutableStateOf(false) }
          FilledTonalButton(onClick = { show = true }) { Text("New note") }
          if (show) NewNoteDialog(
            onDismiss = { show = false },
            onSave = { text ->
              if (text.isNotBlank()) {
                scope.launch {
                  notesRepo.add(text.trim())
                  count = notesRepo.count()
                  recent = notesRepo.list(1)
                }
              }
              show = false
            }
          )
        }
      }

      // Inbox card (H1 right)
      Card(modifier = Modifier.weight(1f)) {
        Column(Modifier.fillMaxWidth().padding(16.dp),
               verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text("Inbox", style = MaterialTheme.typography.titleMedium)
          Text("Your captured notifications", style = MaterialTheme.typography.bodySmall)
          FilledTonalButton(onClick = onOpenInbox) { Text("Open Inbox") }
        }
      }
    }
  }
}

@Composable
fun NewNoteDialog(onDismiss: () -> Unit, onSave: (String) -> Unit) {
  var text by remember { mutableStateOf("") }
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("New Note") },
    text = {
      OutlinedTextField(
        value = text, onValueChange = { text = it },
        placeholder = { Text("Type and save") },
        modifier = Modifier.fillMaxWidth(), minLines = 3
      )
    },
    confirmButton = { TextButton(onClick = { onSave(text) }) { Text("Save") } },
    dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
  )
}

@Composable
fun InboxScreen(onBack: () -> Unit) {
  Scaffold(topBar = {
    TopAppBar(
      title = { Text("Inbox") },
      navigationIcon = {
        TextButton(onClick = onBack) { Text("Back") }
      }
    )
  }) { padding ->
    LazyColumn(Modifier.fillMaxSize().padding(padding)) {
      items(5) { i ->
        ListItem(
          headlineContent = { Text("Notification $i") },
          supportingContent = { Text("Preview text…") },
          trailingContent = {
            FilledIconButton(onClick = { /* TODO: speak item */ }) {
              Icon(Icons.Filled.VolumeUp, contentDescription = "Read")
            }
          }
        )
        Divider()
      }
    }
  }
}

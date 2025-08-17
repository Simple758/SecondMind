@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.secondmind.minimal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.secondmind.minimal.ui.AppInboxScreen
import com.secondmind.minimal.ui.AppSummary

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MaterialTheme {
        val nav = rememberNavController()
        NavHost(navController = nav, startDestination = "home") {

          composable("home") {
            HomeScreen(onOpenInbox = { nav.navigate("inbox") })
          }

          composable("inbox") {
            val sample = listOf(
              AppSummary("com.whatsapp","WhatsApp",3,"DM: Hey, you free?"),
              AppSummary("com.instagram.android","Instagram",2,"New message from Alex"),
              AppSummary("com.twitter.android","X",1,"You were mentioned…")
            )
            Scaffold(topBar = { TopAppBar(title = { Text("Inbox") }) }) { pad ->
              Box(Modifier.padding(pad)) {
                AppInboxScreen(
                  apps = sample,
                  onOpenApp = { /* TODO: navigate to per-app detail */ },
                  onReadApp = { /* TODO: TTS for that app */ }
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun HomeScreen(onOpenInbox: () -> Unit) {
  Scaffold(topBar = { TopAppBar(title = { Text("SecondMind") }) }) { pad ->
    Column(
      modifier = Modifier
        .padding(pad)
        .fillMaxSize()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
      horizontalAlignment = Alignment.Start
    ) {
      QuickNoteCard()
      Button(onClick = onOpenInbox) { Text("Open Inbox") }
    }
  }
}

@Composable
private fun QuickNoteCard() {
  var text by remember { mutableStateOf("") }
  Card {
    Column(
      Modifier.fillMaxWidth().padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Text("Quick Note", style = MaterialTheme.typography.titleMedium)
      OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Type a note…") }
      )
      Text(if (text.isBlank()) "—" else "Saved locally (demo)")
    }
  }
}

package com.secondmind.minimal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InboxScreen(onBack: () -> Unit) {
  Column(
    modifier = Modifier.fillMaxSize().padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Text("Inbox", style = MaterialTheme.typography.titleLarge)
    Text("Your captured notifications will appear here.")
    OutlinedButton(onClick = onBack) { Text("Back") }
  }
}

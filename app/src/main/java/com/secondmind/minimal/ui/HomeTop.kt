package com.secondmind.minimal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeTopCards(
  nav: NavController,
  quickNote: @Composable () -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Left: Notes (reuses your QuickNoteCard)
    Box(modifier = Modifier.weight(1f)) { quickNote() }

    // Right: Inbox card to match H1 look with big action
    Card(
      modifier = Modifier.weight(1f),
      shape = MaterialTheme.shapes.large,
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(
        Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.Start
      ) {
        Text("Inbox", style = MaterialTheme.typography.titleMedium)
        Text("Your captured notifications", style = MaterialTheme.typography.bodySmall)
        FilledTonalButton(
          onClick = { try { nav.navigate("inbox") } catch (_: Throwable) {} },
          shape = MaterialTheme.shapes.extraLarge
        ) { Text("Open Inbox") }
      }
    }
  }
}

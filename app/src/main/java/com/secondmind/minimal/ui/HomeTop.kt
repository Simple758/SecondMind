package com.secondmind.minimal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeTopCards(
  modifier: Modifier = Modifier,
  quickNote: @Composable () -> Unit,
  onOpenInbox: () -> Unit
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Left: Notes (reuses QuickNoteCard)
    Box(modifier = Modifier.weight(1f)) { quickNote() }

    // Right: Inbox card with a single big action
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
          onClick = onOpenInbox,
          shape = MaterialTheme.shapes.extraLarge
        ) { Text("Open Inbox") }
      }
    }
  }
}

package com.secondmind.minimal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun InboxScreen(nav: NavController) {
  Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Column(
      verticalArrangement = Arrangement.spacedBy(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text("Inbox", style = MaterialTheme.typography.headlineSmall)
      Text("Your notifications will appear here.")
      OutlinedButton(onClick = { nav.popBackStack() }) { Text("Back") }
    }
  }
}

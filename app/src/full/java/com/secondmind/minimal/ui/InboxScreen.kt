package com.secondmind.minimal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun InboxScreen(nav: NavController? = null) {
  Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Inbox") }) }) { p ->
    Column(Modifier.fillMaxSize().padding(p).padding(16.dp)) {
      Text("Your notifications will appear here.")
      Spacer(Modifier.height(8.dp))
      OutlinedButton(onClick = { nav?.popBackStack() }) { Text("Back") }
    }
  }
}

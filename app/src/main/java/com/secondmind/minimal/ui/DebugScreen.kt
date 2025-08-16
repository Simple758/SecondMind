package com.secondmind.minimal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DebugScreen(onBack: () -> Unit) {
  Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Debug") }) }) { p ->
    Column(Modifier.fillMaxSize().padding(p).padding(16.dp)) {
      Text("Debug tools coming soon.")
      Spacer(Modifier.height(8.dp))
      OutlinedButton(onClick = onBack) { Text("Back") }
    }
  }
}

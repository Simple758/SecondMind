package com.secondmind.minimal.ui

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(onBack: () -> Unit) {
  Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
    OutlinedButton(onClick = onBack) { Text("Back") }
    Spacer(Modifier.height(12.dp))
    // reuse your existing components
    TtsSettings()
    Spacer(Modifier.height(12.dp))
    TgChannelsSettings()
  }
}

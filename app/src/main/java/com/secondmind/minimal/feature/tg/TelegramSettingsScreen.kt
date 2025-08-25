package com.secondmind.minimal.feature.tg

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TelegramSettingsScreen(onBack: () -> Unit) {
  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = { Text("Telegram") },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
          }
        }
      )
    }
  ) { pad ->
    Column(
      Modifier
        .padding(pad)
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Your existing settings block
      com.secondmind.minimal.ui.TgChannelsSettings()
    }
  }
}

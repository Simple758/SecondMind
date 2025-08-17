package com.secondmind.minimal.dev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
class PatchHarnessActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MaterialTheme {
        Scaffold(
          topBar = { TopAppBar(title = { Text("Patch Harness") }) }
        ) { pad ->
          Column(
            Modifier.padding(pad).fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Text("This is the debug-only patch host inside the base app.")
            // TODO: plug your patch UI here.
          }
        }
      }
    }
  }
}

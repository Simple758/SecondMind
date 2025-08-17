package com.secondmind.minimal.dev

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.VolumeUp
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
            Text("Debug-only patch host inside the base app.")

            // Quick links to required settings screens
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
              Button(onClick = {
                startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
              }) { Text("Notif access") }
              Button(onClick = {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
              }) { Text("A11y settings") }
            }

            // Just proof the icon import works
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
              Icon(Icons.Rounded.VolumeUp, contentDescription = null)
              Spacer(Modifier.width(8.dp))
              Text("Speaker ready")
            }
          }
        }
      }
    }
  }
}

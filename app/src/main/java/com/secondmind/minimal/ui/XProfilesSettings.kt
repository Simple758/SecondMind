package com.secondmind.minimal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.secondmind.minimal.data.Keys
import com.secondmind.minimal.data.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.edit

@Composable
fun XProfilesSettings() {
  val ctx = LocalContext.current
  val scope = rememberCoroutineScope()
  var profiles by remember { mutableStateOf("Bloomberg, Reuters") }
  var count by remember { mutableStateOf(3) }

  LaunchedEffect(Unit) {
    val d = ctx.dataStore.data.first()
    profiles = d[Keys.X_PROFILES] ?: profiles
    count = d[Keys.X_TTS_COUNT] ?: 3
  }

  Text("X Profiles", style = MaterialTheme.typography.titleMedium)
  Spacer(Modifier.height(6.dp))
  Text("Comma-separated handles (no @). Example: Bloomberg, Reuters, TheEconomist", style = MaterialTheme.typography.bodySmall)
  Spacer(Modifier.height(8.dp))
  OutlinedTextField(value = profiles, onValueChange = { profiles = it }, label = { Text("Profiles") }, singleLine = true, modifier = Modifier.fillMaxWidth())
  Spacer(Modifier.height(8.dp))
  Text("Posts to read: $count")
  Slider(value = count.toFloat(), onValueChange = { count = it.toInt().coerceIn(1,5) }, valueRange = 1f..5f, steps = 3)
  Spacer(Modifier.height(8.dp))
  OutlinedButton(onClick = { scope.launch { ctx.dataStore.edit { p -> 
      p[Keys.X_PROFILES] = profiles.trim()
      p[Keys.X_TTS_COUNT] = count
  } } }) { Text("Save") }
}

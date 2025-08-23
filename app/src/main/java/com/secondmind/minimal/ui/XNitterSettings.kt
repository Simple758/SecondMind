package com.secondmind.minimal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.secondmind.minimal.data.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey

private val KEY_NITTER = stringPreferencesKey("x_nitter_base")

@Composable
fun XNitterSettings() {
  val ctx = LocalContext.current
  val scope = rememberCoroutineScope()
  var base by remember { mutableStateOf("") }

  LaunchedEffect(Unit) {
    val d = ctx.dataStore.data.first()
    base = d[KEY_NITTER] ?: ""
  }

  Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
    Text("Nitter base (optional)", style = MaterialTheme.typography.titleMedium)
    Text("Example: https://nitter.net  (leave blank to auto-try mirrors)", style = MaterialTheme.typography.bodySmall)
    OutlinedTextField(
      value = base,
      onValueChange = { base = it },
      label = { Text("Nitter base URL") },
      singleLine = true,
      modifier = Modifier.fillMaxWidth()
    )
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      OutlinedButton(onClick = { scope.launch { ctx.dataStore.edit { it[KEY_NITTER] = base.trim().removeSuffix("/") } } }) { Text("Save") }
      OutlinedButton(onClick = { scope.launch { ctx.dataStore.edit { it.remove(KEY_NITTER) } }; base = "" }) { Text("Clear") }
    }
  }
}

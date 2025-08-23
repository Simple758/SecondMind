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

private val KEY_FEED_URL = stringPreferencesKey("x_feed_url")

@Composable
fun XFeedSettings() {
  val ctx = LocalContext.current
  val scope = rememberCoroutineScope()
  var url by remember { mutableStateOf("") }

  LaunchedEffect(Unit) {
    val d = ctx.dataStore.data.first()
    url = d[KEY_FEED_URL] ?: ""
  }

  Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
    Text("X Feed URL (optional)", style = MaterialTheme.typography.titleMedium)
    Text("Public List or profile URL, e.g. https://x.com/i/lists/123…", style = MaterialTheme.typography.bodySmall)
    OutlinedTextField(
      value = url,
      onValueChange = { url = it },
      label = { Text("X List/Profile URL") },
      singleLine = true,
      modifier = Modifier.fillMaxWidth()
    )
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      OutlinedButton(onClick = { scope.launch { ctx.dataStore.edit { it[KEY_FEED_URL] = url.trim() } } }) { Text("Save") }
      OutlinedButton(onClick = { scope.launch { ctx.dataStore.edit { it.remove(KEY_FEED_URL) } }; url = "" }) { Text("Clear") }
    }
  }
}

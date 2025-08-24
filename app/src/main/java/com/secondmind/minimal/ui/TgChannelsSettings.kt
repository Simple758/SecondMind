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
import androidx.datastore.preferences.core.*

private val TG_CHANNELS     = stringPreferencesKey("tg_channels_csv")   // comma-separated normalized handles
private val TG_PER          = intPreferencesKey("tg_per_channel")       // 1..5
private val TG_ORDER        = stringPreferencesKey("tg_order")          // "newest" | "oldest"
private val TG_OPEN         = stringPreferencesKey("tg_open")           // "app" | "web"
private val TG_PROXY_MODE   = stringPreferencesKey("tg_proxy_mode")     // "auto" | "force" | "direct"
private val TG_TTS_LEN      = intPreferencesKey("tg_tts_len")           // 140..500
private val TG_PREFER_PROXY = booleanPreferencesKey("tg_prefer_proxy")  // bias within auto

@Composable
fun TgChannelsSettings() {
  val ctx = LocalContext.current
  val scope = rememberCoroutineScope()

  var handles by remember { mutableStateOf("") }
  var per by remember { mutableStateOf(3) }
  var order by remember { mutableStateOf("newest") }
  var open by remember { mutableStateOf("app") }
  var proxy by remember { mutableStateOf("auto") }
  var preferProxy by remember { mutableStateOf(false) }
  var ttsLen by remember { mutableStateOf(300) }

  LaunchedEffect(Unit) {
    val d = ctx.dataStore.data.first()
    handles = d[TG_CHANNELS] ?: ""
    per = d[TG_PER] ?: 3
    order = d[TG_ORDER] ?: "newest"
    open = d[TG_OPEN] ?: "app"
    proxy = d[TG_PROXY_MODE] ?: "auto"
    ttsLen = d[TG_TTS_LEN] ?: 300
    preferProxy = d[TG_PREFER_PROXY] ?: false
  }

  fun normalizeInput(raw: String): String {
    if (raw.isBlank()) return ""
    val out = LinkedHashSet<String>()
    val parts = raw.split(',', ' ', '\n', '\t').map { it.trim() }.filter { it.isNotBlank() }
    val rgx = Regex("^[A-Za-z0-9_]{3,64}$")
    parts.forEach { token ->
      var t = token
        .removePrefix("@")
        .replace(Regex("^https?://t\\.me/(s/)?"), "")
        .replace(Regex("^tg://resolve\\?domain="), "")
        .substringBefore('?')
        .substringBefore('/')
        .trim()
        .lowercase()
      if (rgx.matches(t)) out += t
    }
    return out.joinToString(", ")
  }

  Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
    Text("Telegram Channels", style = MaterialTheme.typography.titleMedium)

    OutlinedTextField(
      value = handles,
      onValueChange = { handles = it },
      label = { Text("Paste @handle or t.me link (comma-separated)") },
      singleLine = false,
      modifier = Modifier.fillMaxWidth()
    )

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      OutlinedButton(onClick = { handles = normalizeInput(handles) }) { Text("Normalize") }
      OutlinedButton(onClick = {
        scope.launch {
          ctx.dataStore.edit {
            it[TG_CHANNELS] = normalizeInput(handles)
            it[TG_PER] = per.coerceIn(1,5)
            it[TG_ORDER] = order
            it[TG_OPEN] = open
            it[TG_PROXY_MODE] = proxy
            it[TG_TTS_LEN] = ttsLen.coerceIn(140, 500)
            it[TG_PREFER_PROXY] = preferProxy
          }
        }
      }) { Text("Save") }
      OutlinedButton(onClick = {
        handles = ""
        scope.launch { ctx.dataStore.edit { prefs ->
          prefs.remove(TG_CHANNELS)
        } }
      }) { Text("Clear") }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      Text("Posts/channel: $per")
      OutlinedButton(onClick = { per = (per - 1).coerceAtLeast(1) }) { Text("-") }
      OutlinedButton(onClick = { per = (per + 1).coerceAtMost(5) }) { Text("+") }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      Text("Read order:")
      OutlinedButton(onClick = { order = "newest" }, enabled = order != "newest") { Text("Newest") }
      OutlinedButton(onClick = { order = "oldest" }, enabled = order != "oldest") { Text("Oldest") }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      Text("Open with:")
      OutlinedButton(onClick = { open = "app" }, enabled = open != "app") { Text("Telegram app") }
      OutlinedButton(onClick = { open = "web" }, enabled = open != "web") { Text("Web") }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      Text("Proxy mode:")
      OutlinedButton(onClick = { proxy = "auto" }, enabled = proxy != "auto") { Text("Auto") }
      OutlinedButton(onClick = { proxy = "force" }, enabled = proxy != "force") { Text("Force") }
      OutlinedButton(onClick = { proxy = "direct" }, enabled = proxy != "direct") { Text("Direct") }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      Text("Prefer proxy in Auto:")
      Switch(checked = preferProxy, onCheckedChange = { preferProxy = it })
    }

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      Text("Max TTS len: $ttsLen")
      OutlinedButton(onClick = { ttsLen = (ttsLen - 20).coerceAtLeast(140) }) { Text("-") }
      OutlinedButton(onClick = { ttsLen = (ttsLen + 20).coerceAtMost(500) }) { Text("+") }
    }
  }
}

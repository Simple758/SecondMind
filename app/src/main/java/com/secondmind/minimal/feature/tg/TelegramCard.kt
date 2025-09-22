package com.secondmind.minimal.feature.tg
import androidx.compose.ui.graphics.Color

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.secondmind.minimal.data.dataStore
import com.secondmind.minimal.tts.Reader
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.*
import android.content.Intent
import android.net.Uri

private val TG_CHANNELS     = stringPreferencesKey("tg_channels_csv")
private val TG_PER          = intPreferencesKey("tg_per_channel")
private val TG_ORDER        = stringPreferencesKey("tg_order")
private val TG_OPEN         = stringPreferencesKey("tg_open")
private val TG_PROXY_MODE   = stringPreferencesKey("tg_proxy_mode")
private val TG_TTS_LEN      = intPreferencesKey("tg_tts_len")
private val TG_PREFER_PROXY = booleanPreferencesKey("tg_prefer_proxy")

@Composable
fun TelegramCard(modifier: Modifier = Modifier) {
  val ctx = LocalContext.current
  val scope = rememberCoroutineScope()

  var handles by remember { mutableStateOf(listOf<String>()) }
  var per by remember { mutableStateOf(3) }
  var order by remember { mutableStateOf("newest") }
  var open by remember { mutableStateOf("app") }
  var proxy by remember { mutableStateOf("auto") }
  var preferProxy by remember { mutableStateOf(false) }
  var ttsLen by remember { mutableStateOf(300) }

  var items by remember { mutableStateOf(listOf<TgPost>()) }
  var loading by remember { mutableStateOf(false) }
  var error by remember { mutableStateOf<String?>(null) }
  var reading by remember { mutableStateOf(false) }

  LaunchedEffect(Unit) {
    val d = ctx.dataStore.data.first()
    handles = (d[TG_CHANNELS] ?: "").split(',').map { it.trim() }.filter { it.isNotBlank() }
    per = (d[TG_PER] ?: 3).coerceIn(1,5)
    order = d[TG_ORDER] ?: "newest"
    open = d[TG_OPEN] ?: "app"
    proxy = d[TG_PROXY_MODE] ?: "auto"
    preferProxy = d[TG_PREFER_PROXY] ?: false
    ttsLen = (d[TG_TTS_LEN] ?: 300).coerceIn(140,500)
  }

  fun toDeepLink(url: String?): Uri? {
    if (url.isNullOrBlank()) return null
    // https://t.me/(s/)?<handle>/<id>
    val re = Regex("^https?://t\\.me/(?:s/)?([A-Za-z0-9_]+)/?(\\d+)?")
    val m = re.find(url) ?: return Uri.parse(url)
    val handle = m.groupValues.getOrNull(1)?.ifBlank { null } ?: return Uri.parse(url)
    val id = m.groupValues.getOrNull(2)?.toLongOrNull()
    return if (id != null) {
      Uri.parse("tg://resolve?domain=$handle&post=$id")
    } else {
      Uri.parse("tg://resolve?domain=$handle")
    }
  }

  fun openPost(url: String?) {
    if (url.isNullOrBlank()) return
    val intent = if (open == "app") {
      Intent(Intent.ACTION_VIEW, toDeepLink(url))
    } else {
      Intent(Intent.ACTION_VIEW, Uri.parse(url))
    }
    try { ctx.startActivity(intent) } catch (_: Throwable) { /* ignore */ }
  }

  fun refresh() {
    loading = true; error = null
    scope.launch {
      runCatching {
        if (handles.isEmpty()) return@runCatching emptyList<TgPost>()
        TelegramRepo.fetchMany(handles, per, mode = proxy, preferProxy = preferProxy)
      }.onSuccess { list ->
        // Order per setting (we already sorted newest-first in repo)
        items = if (order == "oldest") list.asReversed() else list
      }.onFailure { e ->
        error = e.message ?: "Failed to load."
      }
      loading = false
    }
  }

  LaunchedEffect(handles, per, order, proxy, preferProxy) { refresh() }

  Card(modifier = modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.Black)) {
    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("Telegram", style = MaterialTheme.typography.titleLarge)
        IconButton(onClick = {
          if (reading) { Reader.stop(); reading = false; return@IconButton }
          if (items.isEmpty()) { refresh(); return@IconButton }
          val txt = items.take(8).joinToString(". ") { "${it.channel}: ${it.text}" }
            .replace("\\s+".toRegex(), " ").take(ttsLen)
          Reader.speak(ctx, txt); reading = true
        }) {
          Icon(
            painter = painterResource(id = if (reading) android.R.drawable.ic_lock_silent_mode else android.R.drawable.ic_lock_silent_mode_off),
            contentDescription = if (reading) "Stop" else "Read"
          )
        }
      }

      if (handles.isEmpty()) {
        Text("Add channels in Settings → Telegram Channels.", style = MaterialTheme.typography.bodySmall)
      }

      if (loading && items.isEmpty()) LinearProgressIndicator(Modifier.fillMaxWidth())

      items.take(6).forEach { p ->
        OutlinedButton(
          onClick = { openPost(p.link) },
          modifier = Modifier.fillMaxWidth()
        ) { Text("${p.channel}: ${p.text}") }
      }

      if (error != null) Text("Error: $error", style = MaterialTheme.typography.bodySmall)

      Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedButton(onClick = { refresh() }) { Text("Refresh") }
      }
    }
  }
}

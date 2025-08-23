package com.secondmind.minimal.feature.x

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.secondmind.minimal.data.Keys
import com.secondmind.minimal.data.dataStore
import com.secondmind.minimal.tts.Reader
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun XCard(modifier: Modifier = Modifier) {
  val ctx = LocalContext.current
  val scope = rememberCoroutineScope()

  // Prefs
  var listUrl by remember { mutableStateOf("") }
  var channelsCsv by remember { mutableStateOf("") }
  var nitter by remember { mutableStateOf("") }
  var readCount by remember { mutableStateOf(3) }

  LaunchedEffect(Unit) {
    val data = ctx.dataStore.data.first()
    listUrl = data[Keys.X_FEED_URL] ?: ""
    channelsCsv = data[Keys.X_PROFILES] ?: "Bloomberg, Reuters"
    nitter = data[Keys.X_NITTER_BASE] ?: ""
    readCount = data[Keys.X_TTS_COUNT] ?: 3
  }

  val channels = remember(channelsCsv) {
    channelsCsv.split(",").map { it.trim() }.filter { it.isNotBlank() }
  }
  var active by remember { mutableStateOf(0) }
  var posts by remember { mutableStateOf(listOf<XPost>()) }
  var error by remember { mutableStateOf<String?>(null) }
  var reading by remember { mutableStateOf(false) }
  var loading by remember { mutableStateOf(false) }

  fun fetch() {
    loading = true; error = null
    val count = readCount.coerceIn(1, 5)
    scope.launch {
      runCatching {
        when {
          listUrl.isNotBlank() && active == 0 -> XRepo.fetchList(nitter, listUrl, count = count)
          channels.isNotEmpty() -> XRepo.fetchProfiles(nitter, channels, perUser = 2, totalLimit = count)
          else -> XRepo.searchFallback(nitter, "news", count)
        }
      }.onSuccess { posts = it }
       .onFailure { e -> error = e.message ?: "Failed to load." }
      loading = false
    }
  }

  LaunchedEffect(active, channelsCsv, listUrl, readCount) { fetch() }

  Card(modifier = modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("X Timeline", style = MaterialTheme.typography.titleLarge)
        IconButton(onClick = {
          if (reading) { Reader.stop(); reading = false; return@IconButton }
          if (posts.isEmpty()) { fetch(); return@IconButton }
          val msg = posts.take(readCount.coerceIn(1,5)).joinToString(". ") { p ->
            (p.author?.let { "$it: " } ?: "") + p.text
          }.replace("\\s+".toRegex(), " ").trim().take(1000)
          Reader.speak(ctx, msg); reading = true
        }) {
          Icon(
            painter = painterResource(id = if (reading) android.R.drawable.ic_lock_silent_mode else android.R.drawable.ic_lock_silent_mode_off),
            contentDescription = if (reading) "Stop" else "Read"
          )
        }
      }

      // Tabs: List (if configured) + Profiles
      val tabs = if (listUrl.isNotBlank()) listOf("List", "Profiles") else listOf("Profiles")
      var tab by remember { mutableStateOf(0) }
      ScrollableTabRow(selectedTabIndex = tab, edgePadding = 0.dp) {
        tabs.forEachIndexed { i, s -> Tab(selected = tab == i, onClick = { tab = i; Reader.stop(); reading = false }, text = { Text(s) }) }
      }
      active = tab

      if (loading && posts.isEmpty()) LinearProgressIndicator(Modifier.fillMaxWidth())

      posts.take(3).forEachIndexed { idx, p ->
        Text("${idx+1}. ${(p.author ?: "X")}: ${p.text}", style = MaterialTheme.typography.bodyMedium)
      }

      if (error != null) Text("Error: $error", style = MaterialTheme.typography.bodySmall)
      Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedButton(onClick = { fetch() }) { Text("Refresh") }
      }
    }
  }
}

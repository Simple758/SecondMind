package com.secondmind.minimal.feature.news

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.secondmind.minimal.tts.Reader
import kotlinx.coroutines.launch

@Composable
fun NewsCard(modifier: Modifier = Modifier) {
  val ctx = LocalContext.current
  val scope = rememberCoroutineScope()

  val sectors = listOf("Tech", "Markets", "World", "Sports", "Science/AI")
  var tab by remember { mutableStateOf(0) }
  var items by remember { mutableStateOf<List<NewsItem>>(emptyList()) }
  var reading by remember { mutableStateOf(false) }
  var error by remember { mutableStateOf<String?>(null) }
  var loading by remember { mutableStateOf(false) }

  fun fetch() {
    loading = true; error = null
    scope.launch {
      runCatching { NewsRepo.fetch(sectors[tab], limit = 5) }
        .onSuccess { items = it }
        .onFailure { e -> error = e.message ?: "Failed to load." }
      loading = false
    }
  }

  LaunchedEffect(tab) { fetch() }

  Card(modifier = modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("News", style = MaterialTheme.typography.titleLarge)
        IconButton(onClick = {
          if (reading) { Reader.stop(); reading = false; return@IconButton }
          if (items.isEmpty()) { fetch(); return@IconButton }
          val msg = items.take(3).joinToString(". ") { i -> "${i.source}: ${i.title}" }
            .replace("\\s+".toRegex(), " ").trim().take(1000)
          Reader.speak(ctx, msg); reading = true
        }) {
          Icon(
            painter = painterResource(id = if (reading) android.R.drawable.ic_lock_silent_mode else android.R.drawable.ic_lock_silent_mode_off),
            contentDescription = if (reading) "Stop" else "Read"
          )
        }
      }

      ScrollableTabRow(selectedTabIndex = tab, edgePadding = 0.dp) {
        sectors.forEachIndexed { i, s -> Tab(selected = tab == i, onClick = { tab = i; Reader.stop(); reading = false }, text = { Text(s) }) }
      }

      if (loading && items.isEmpty()) LinearProgressIndicator(Modifier.fillMaxWidth())
      items.take(3).forEachIndexed { idx, it -> Text("${idx+1}. ${it.title} — ${it.source}", style = MaterialTheme.typography.bodyMedium) }
      if (error != null) Text("Error: $error", style = MaterialTheme.typography.bodySmall)
      Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedButton(onClick = {
          items.firstOrNull()?.let { h ->
            try { ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(h.link))) } catch (_: Throwable) {}
          }
        }) { Text("Open") }
        OutlinedButton(onClick = { fetch() }) { Text("Refresh") }
      }
    }
  }
}

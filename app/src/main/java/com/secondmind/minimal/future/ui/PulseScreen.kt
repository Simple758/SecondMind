package com.secondmind.minimal.future.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import com.secondmind.minimal.future.db.*
import com.secondmind.minimal.future.engine.SeedEvaluator
import androidx.compose.ui.platform.LocalContext

@Composable
fun PulseScreen(nav: NavHostController? = null) {
  val ctx = LocalContext.current
  val scope = rememberCoroutineScope()
  var top by remember { mutableStateOf(listOf<SignalEntity>()) }

  LaunchedEffect(true) {
    // Evaluate seeds then load signals
    SeedEvaluator.runOnce(ctx)
    val repo = FutureRepos(ctx)
    top = repo.signals.top(limit = 10)
  }

  Column(Modifier.fillMaxSize().padding(16.dp)) {
    Text("Pulse", style = MaterialTheme.typography.titleLarge)
    Spacer(Modifier.height(8.dp))
    LazyColumn(Modifier.weight(1f)) {
      items(count = top.size) { i ->
        val s = top[i]
        Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
          Text(s.title, style = MaterialTheme.typography.titleMedium)
          Text("Due: ${s.dueAt}")
        }
        Divider()
      }
    }
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      Button(onClick = {
        scope.launch {
          SeedEvaluator.runOnce(ctx)
          val repo = FutureRepos(ctx)
          top = repo.signals.top(limit = 10)
        }
      }) { Text("Refresh") }
      Button(onClick = { nav?.popBackStack() }) { Text("Back") }
    }
  }
}

import androidx.lifecycle.viewModelScope
package com.secondmind.minimal.dev

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class Msg(val pkg: String, val ts: Long, val title: String, val text: String)
data class AppGroup(val pkg: String, val label: String, val messages: List<Msg>)

class PatchInboxViewModel(private val app: Application) : ViewModel() {
  private val _groups = mutableStateListOf<AppGroup>()
  val groups: List<AppGroup> get() = _groups

  private val map = linkedMapOf<String, MutableList<Msg>>() // pkg -> msgs

  init {
    // Collect notifications and update UI state
    PatchNotifBus.events.onEach { sbn ->
      val n = sbn.notification
      val title = n.extras.getCharSequence("android.title")?.toString().orEmpty()
      val text = n.extras.getCharSequence("android.text")?.toString().orEmpty()
      val m = Msg(sbn.packageName, sbn.postTime, title, text)
      val list = map.getOrPut(m.pkg) { mutableListOf() }
      list.add(0, m)
      if (list.size > 20) list.removeLast()

      // rebuild grouped snapshot sorted by newest
      val pm = app.packageManager
      val snap = map.entries.map { (pkg, msgs) ->
        val label = try { pm.getApplicationLabel(pm.getApplicationInfo(pkg, 0)).toString() } catch (_: Throwable) { pkg }
        AppGroup(pkg, label, msgs.toList())
      }.sortedByDescending { it.messages.firstOrNull()?.ts ?: 0L }

      _groups.clear(); _groups.addAll(snap)
    }.launchIn(viewModelScope)
  }
}

@OptIn(ExperimentalMaterial3Api::class)
class PatchHarnessActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MaterialTheme {
        val ctx = LocalContext.current
        val speaker = remember { TtsSpeaker(ctx) }
        DisposableEffect(Unit) { onDispose { speaker.shutdown() } }

        val vm: PatchInboxViewModel = viewModel(factory = object: ViewModelProvider.Factory {
          override fun <T : ViewModel> create(c: Class<T>): T =
            PatchInboxViewModel(application as Application) as T
        })
        val groups by remember { derivedStateOf { vm.groups } }

        Scaffold(
          topBar = {
            TopAppBar(
              title = { Text("Patch Harness — Inbox") },
              actions = {
                TextButton(onClick = {
                  ctx.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                }) { Text("Notif Access") }
              }
            )
          }
        ) { pad ->
          LazyColumn(
            modifier = Modifier.padding(pad).fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            items(groups.size) { i ->
              val g = groups[i]
              Card(Modifier.fillMaxWidth()) {
                Row(
                  Modifier.fillMaxWidth().padding(16.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Column(Modifier.weight(1f)) {
                    Text(g.label, style = MaterialTheme.typography.titleMedium)
                    Text("${g.messages.size} notifications", style = MaterialTheme.typography.bodySmall)
                    val preview = g.messages.firstOrNull()?.let { m ->
                      (if (m.title.isNotBlank()) "${m.title} — " else "") + m.text
                    }.orEmpty()
                    if (preview.isNotBlank()) {
                      Text(preview, maxLines = 1, overflow = TextOverflow.Ellipsis,
                          style = MaterialTheme.typography.labelSmall)
                    }
                  }
                  IconButton(onClick = {
                    val text = buildString {
                      append(g.label).append(". ")
                      g.messages.take(5).forEachIndexed { idx, m ->
                        if (m.title.isNotBlank()) append(m.title).append(". ")
                        append(m.text)
                        if (idx < g.messages.lastIndex) append(". ")
                      }
                    }
                    speaker.speak(text)
                  }) { Icon(Icons.Rounded.VolumeUp, contentDescription = "Read all") }
                }
              }
            }
          }
        }
      }
    }
  }
}

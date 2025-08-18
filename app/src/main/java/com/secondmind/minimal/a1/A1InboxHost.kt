@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.secondmind.minimal.a1

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun A1InboxHost() {
  val app = androidx.compose.ui.platform.LocalContext.current.applicationContext as Application
  val vm: A1ViewModel = viewModel(factory = object: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = A1ViewModel(app) as T
  })
  val groups by vm.groups.collectAsState()
  val speaker = remember(app) { TtsSpeaker(app) }
  DisposableEffect(Unit) { onDispose { speaker.shutdown() } }

  Scaffold(topBar = { TopAppBar(title = { Text("Inbox (A1)") }) }) { pad ->
    Column(Modifier.padding(pad).fillMaxSize()) {
      A1Screen(
        groups = groups,
        onOpenApp = { /* optional: navigate */ },
        onReadApp = { g ->
          val text = buildString {
            append(g.appLabel).append(". ")
            g.messages.take(5).forEachIndexed { idx, m ->
              if (m.title.isNotBlank()) append(m.title).append(". ")
              append(m.text)
              if (idx < g.messages.lastIndex) append(". ")
            }
          }
          speaker.speak(text)
        }
      )
    }
  }
}

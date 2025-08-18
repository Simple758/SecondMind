package com.secondmind.minimal.dev

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.secondmind.minimal.dev.ui.AppInboxScreen
import com.secondmind.minimal.dev.tts.TtsSpeaker

@OptIn(ExperimentalMaterial3Api::class)
class PatchHarnessActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val ctx = LocalContext.current
                val app = ctx.applicationContext as Application

                val vm: PatchInboxViewModel = viewModel(factory = object: ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T =
                        PatchInboxViewModel(app) as T
                })
                val groups by vm.groups.collectAsState()

                val speaker = remember(ctx) { TtsSpeaker(ctx) }
                DisposableEffect(Unit) { onDispose { speaker.shutdown() } }

                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text("Patch Harness") },
                            actions = {
                                Row(Modifier.padding(end = 8.dp)) {
                                    Button(onClick = {
                                        ctx.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                                    }) { Text("Notif access") }
                                    Spacer(Modifier.width(8.dp))
                                    Button(onClick = {
                                        ctx.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                                    }) { Text("A11y settings") }
                                }
                            }
                        )
                    }
                ) { pad ->
                    Column(Modifier.padding(pad).fillMaxSize()) {
                        AppInboxScreen(
                            groups = groups,
                            onOpenApp = { /* no-op */ },
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
        }
    }
}

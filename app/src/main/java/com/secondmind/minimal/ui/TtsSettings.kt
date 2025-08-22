package com.secondmind.minimal.ui

import android.content.Intent
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.secondmind.minimal.data.Keys
import com.secondmind.minimal.data.dataStore
import com.secondmind.minimal.tts.Reader
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.edit

@Composable
fun TtsSettings() {
  val ctx = LocalContext.current
  val scope = rememberCoroutineScope()

  val voiceFlow = remember { ctx.dataStore.data.map { it[Keys.READER_VOICE] ?: "" } }
  val selected by voiceFlow.collectAsState(initial = "")

  var voices by remember { mutableStateOf<List<Voice>>(emptyList()) }
  LaunchedEffect(Unit) {
    val google = "com.google.android.tts"
    var tts: TextToSpeech? = null
    try {
      tts = try { TextToSpeech(ctx.applicationContext, TextToSpeech.OnInitListener { _ -> }, google) }
            catch (_: Throwable) { TextToSpeech(ctx.applicationContext, TextToSpeech.OnInitListener { _ -> }) }
      voices = (tts?.voices?.toList() ?: emptyList()).sortedBy { it.name }
    } catch (_: Throwable) {
      voices = emptyList()
    } finally {
      try { tts?.shutdown() } catch (_: Throwable) {}
    }
  }

  var expanded by remember { mutableStateOf(false) }

  Text("TTS Voice")
  Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
    OutlinedButton(onClick = { expanded = true }) {
      Text(if (selected.isBlank()) "(Default)" else selected)
    }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
      voices.forEach { v ->
        DropdownMenuItem(
          text = { Text(v.name) },
          onClick = {
            expanded = false
            scope.launch { ctx.dataStore.edit { prefs -> prefs[Keys.READER_VOICE] = v.name } }
            Reader.updateVoice(v.name, ctx)
          }
        )
      }
    }
  }

  Spacer(Modifier.height(8.dp))
  Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
    OutlinedButton(onClick = { try { ctx.startActivity(Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA)) } catch (_: Throwable) {} }) { Text("Install voices") }
    OutlinedButton(onClick = {
      try { ctx.startActivity(Intent("com.android.settings.TTS_SETTINGS")) }
      catch (_: Throwable) { try { ctx.startActivity(Intent(Settings.ACTION_SETTINGS)) } catch (_: Throwable) {} }
    }) { Text("System TTS Settings") }
  }

  Spacer(Modifier.height(8.dp))
  OutlinedButton(onClick = { Reader.speak(ctx, "This is a test of your selected voice in SecondMind.") }) {
    Text("Test TTS")
  }
}

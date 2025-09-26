package com.secondmind.minimal.ai

import android.os.Bundle
import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.util.Locale
import com.secondmind.minimal.core.ai.*

class AiChatActivity : ComponentActivity() {
  private var tts: TextToSpeech? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    tts = TextToSpeech(this) { status -> if (status == TextToSpeech.SUCCESS) tts?.language = Locale.getDefault() }
    setContent {
      MaterialTheme { Surface { AiChatScreen(onSpeak = { tts?.speak(it, TextToSpeech.QUEUE_FLUSH, null, "ai-chat") }, context = this) } }
    }
  }
  override fun onDestroy() { tts?.stop(); tts?.shutdown(); super.onDestroy() }
}

data class ChatMessage(val role: String, val content: String)

@Composable
fun AiChatScreen(onSpeak: (String) -> Unit, context: Context) {
  val scope = rememberCoroutineScope()
  var input by remember { mutableStateOf(TextFieldValue("")) }
  val messages = remember { mutableStateListOf<ChatMessage>() }
  var sending by remember { mutableStateOf(false) }
  var speakEnabled by remember { mutableStateOf(true) }

  fun sendPrompt(text: String) {
    if (text.isBlank()) return
    messages.add(ChatMessage("user", text))
    sending = true
    scope.launch {
      val res = AIServiceLocator.get().complete(
        context = context,
        prompt = Prompt(system = "You are a concise helpful assistant inside an Android app.", user = text),
        options = AIOptions(model = "deepseek-chat", maxTokens = 400, temperature = 0.3),
        packet = ContextPacket(source = ContextPacket.Source.UI, appPackage = context.packageName, text = text)
      )
      val reply = when (res) { is AIResult.Text -> res.content ; is AIResult.Error -> "Error: ${res.message}" }
      messages.add(ChatMessage("assistant", reply))
      if (speakEnabled && res is AIResult.Text) onSpeak(reply)
      sending = false
    }
  }

  Column(Modifier.fillMaxSize().padding(12.dp)) {
    Text("AI Chat", style = MaterialTheme.typography.titleLarge); Spacer(Modifier.height(8.dp))
    Row { FilterChip(selected = speakEnabled, onClick = { speakEnabled = !speakEnabled }, label = { Text(if (speakEnabled) "TTS: ON" else "TTS: OFF") }) }
    Spacer(Modifier.height(8.dp)); Divider()
    LazyColumn(Modifier.weight(1f).fillMaxWidth(), contentPadding = PaddingValues(vertical = 8.dp)) {
      items(messages) { m ->
        val bg = if (m.role == "user") MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant
        Surface(color = bg, tonalElevation = 2.dp, shape = MaterialTheme.shapes.medium, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
          Column(Modifier.padding(12.dp)) { Text(if (m.role=="user") "You" else "DeepSeek", style = MaterialTheme.typography.labelMedium); Spacer(Modifier.height(4.dp)); Text(m.content) }
        }
      }
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      OutlinedTextField(value = input, onValueChange = { input = it }, modifier = Modifier.weight(1f), singleLine = true, placeholder = { Text("Type a message…") })
      Button(enabled = !sending, onClick = { val text = input.text.trim(); input = TextFieldValue(""); sendPrompt(text) }) { Text(if (sending) "…" else "Send") }
    }
  }
}

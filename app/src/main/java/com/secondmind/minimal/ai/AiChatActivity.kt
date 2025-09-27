package com.secondmind.minimal.ai

import android.os.Bundle
import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import kotlinx.coroutines.launch
import java.util.Locale
import com.secondmind.minimal.core.ai.*

class AiChatActivity : ComponentActivity() {
  private var tts: TextToSpeech? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    tts = TextToSpeech(this) { status ->
      if (status == TextToSpeech.SUCCESS) {
        try { tts?.language = Locale.getDefault() } catch (_: Throwable) {}
      }
    }
    setContent {
      MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
          AiChatScreen(
            context = this,
            onSpeak = { text -> tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ai-${text.hashCode()}") }
          )
        }
      }
    }
  }
  override fun onDestroy() { tts?.stop(); tts?.shutdown(); super.onDestroy() }
}

data class ChatMessage(val role: String, val content: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiChatScreen(onSpeak: (String) -> Unit, context: Context) {
  val scope = rememberCoroutineScope()
  var input by remember { mutableStateOf(TextFieldValue("")) }
  val messages = remember { mutableStateListOf<ChatMessage>() }
  var sending by remember { mutableStateOf(false) }

  // Simple in-memory model selector; add more if needed
  val models = listOf("deepseek-chat", "deepseek-reasoner")
  var selectedModel by remember { mutableStateOf(models.first()) }
  var dropExpanded by remember { mutableStateOf(false) }

  Column(Modifier.fillMaxSize().padding(12.dp)) {
    // Header
    Row(
      Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text("AI Chat", style = MaterialTheme.typography.titleLarge, color = Color.White)
      ExposedDropdownMenuBox(expanded = dropExpanded, onExpandedChange = { dropExpanded = !dropExpanded }) {
        OutlinedTextField(
          readOnly = true,
          value = selectedModel,
          onValueChange = {},
          label = { Text("Model") },
          trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropExpanded) },
          modifier = Modifier.menuAnchor().widthIn(min = 180.dp)
        )
        ExposedDropdownMenu(expanded = dropExpanded, onDismissRequest = { dropExpanded = false }) {
          models.forEach { m ->
            DropdownMenuItem(
              text = { Text(m) },
              onClick = { selectedModel = m; dropExpanded = false }
            )
          }
        }
      }
    }

    Spacer(Modifier.height(8.dp))
    Divider(color = Color(0x22FFFFFF))

    // Messages
    LazyColumn(
      Modifier.weight(1f).fillMaxWidth(),
      contentPadding = PaddingValues(vertical = 8.dp)
    ) {
      itemsIndexed(messages) { _, m ->
        val isUser = m.role == "user"
        val bubbleColor = if (isUser) Color(0xFF3B2E5A) else Color(0xFF2A2A2A)
        val label = if (isUser) "You" else "DeepSeek"

        Surface(
          color = bubbleColor,
          tonalElevation = 2.dp,
          shape = MaterialTheme.shapes.medium,
          modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        ) {
          Column(Modifier.padding(12.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
              Text(label, style = MaterialTheme.typography.labelMedium, color = Color.White)
              if (!isUser) {
                IconButton(onClick = { onSpeak(m.content) }) {
                  Icon(Icons.Filled.VolumeUp, contentDescription = "Speak", tint = Color.White)
                }
              }
            }
            Spacer(Modifier.height(4.dp))
            Text(m.content, color = Color.White)
          }
        }
      }
    }

    // Input
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      OutlinedTextField(
        value = input,
        onValueChange = { input = it },
        modifier = Modifier.weight(1f),
        singleLine = true,
        placeholder = { Text("Type a message…") }
      )
      Button(
        enabled = !sending,
        onClick = {
          val text = input.text.trim()
          if (text.isNotEmpty()) {
            input = TextFieldValue("")
            messages.add(ChatMessage("user", text))
            sending = true
            scope.launch {
              val res = AIServiceLocator.get().complete(
                context = context,
                prompt = Prompt(system = "You are a concise helpful assistant inside an Android app.", user = text),
                options = AIOptions(model = selectedModel, maxTokens = 400, temperature = 0.3),
                packet = ContextPacket(source = ContextPacket.Source.UI, appPackage = context.packageName, text = text)
              )
              val reply = when (res) {
                is AIResult.Text -> res.content
                is AIResult.Error -> "Error: ${res.message}"
              }
              messages.add(ChatMessage("assistant", reply))
              sending = false
            }
          }
        }
      ) { Text(if (sending) "…" else "Send") }
    }
  }
}

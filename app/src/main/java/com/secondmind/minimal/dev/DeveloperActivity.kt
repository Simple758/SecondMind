package com.secondmind.minimal.dev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com.secondmind.minimal.core.ai.*

class DeveloperActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
          DevScreen()
        }
      }
    }
  }
}

@Composable
private fun DevScreen() {
  val ctx = LocalContext.current
  val scope = rememberCoroutineScope()
  var status by remember { mutableStateOf<String?>(null) }
  var busy by remember { mutableStateOf(false) }

  Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Text("Developer Tools", style = MaterialTheme.typography.titleLarge, color = Color.White)
    Divider(color = Color(0x22FFFFFF))
    Text("DeepSeek Connectivity", style = MaterialTheme.typography.titleMedium, color = Color.White)

    OutlinedButton(
      enabled = !busy,
      onClick = {
        busy = true; status = "Calling…"
        scope.launch {
          val res = AIServiceLocator.get().complete(
            context = ctx,
            prompt = Prompt(system = "Reply only with the single word: pong.", user = "ping"),
            options = AIOptions(model = "deepseek-chat", maxTokens = 8),
            packet = ContextPacket(source = ContextPacket.Source.UI, appPackage = ctx.packageName, text = "dev-ping")
          )
          status = when (res) { is AIResult.Text -> "OK: ${res.content}" ; is AIResult.Error -> "Error: ${res.message}" }
          busy = false
        }
      }
    ) { Text(if (busy) "Testing…" else "Ping DeepSeek") }

    if (status != null) Text(status!!, color = Color.White)
  }
}

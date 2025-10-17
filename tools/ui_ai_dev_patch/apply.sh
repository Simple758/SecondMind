#!/usr/bin/env bash
set -Eeuo pipefail
set +H

# Repo root
cd "$(git rev-parse --show-toplevel 2>/dev/null || echo "$HOME/SecondMind")"
echo "== Repo: $(pwd)"

# --- 0) Helpers ---
insert_import_once () {
  local file="$1"; local import_stmt="$2"
  if ! grep -qE "^[[:space:]]*${import_stmt//\*/\\*}[[:space:]]*$" "$file"; then
    awk -v im="$import_stmt" 'BEGIN{added=0} {print; if(!added && NR==4){print im; added=1}}' "$file" > "$file.__tmp__" && mv "$file.__tmp__" "$file"
  fi
}

# --- 1) Add Activities (DeveloperActivity + AiChatActivity) ---
mkdir -p app/src/main/java/com/secondmind/minimal/dev
mkdir -p app/src/main/java/com/secondmind/minimal/ai

# DeveloperActivity
cat > app/src/main/java/com/secondmind/minimal/dev/DeveloperActivity.kt << "KTX"
package com.secondmind.minimal.dev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com.secondmind.minimal.core.ai.*

class DeveloperActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MaterialTheme {
        Surface {
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
    Text("Developer Tools", style = MaterialTheme.typography.titleLarge)
    Divider()
    Text("DeepSeek Connectivity", style = MaterialTheme.typography.titleMedium)
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

    if (status != null) Text(status!!)
  }
}
KTX

# AiChatActivity
cat > app/src/main/java/com/secondmind/minimal/ai/AiChatActivity.kt << "KTX"
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
KTX

# --- 2) Manifest entries ---
MAN="app/src/main/AndroidManifest.xml"
[ -f "$MAN" ] || { echo "❌ $MAN not found"; exit 1; }
if ! grep -q "com.secondmind.minimal.ai.AiChatActivity" "$MAN"; then
  awk '
    BEGIN{done=0}
    /<\/application>/ && !done {
      print "    <activity android:name=\"com.secondmind.minimal.ai.AiChatActivity\" android:exported=\"false\" android:label=\"AI Chat\" />"
      print "    <activity android:name=\"com.secondmind.minimal.dev.DeveloperActivity\" android:exported=\"false\" android:label=\"Developer\" />"
      print
      done=1; next
    }
    { print }
  ' "$MAN" > "$MAN.__tmp__" && mv "$MAN.__tmp__" "$MAN"
fi

# --- 3) Patch MainActivity: add routes that launch the activities ---
MAIN="$(git ls-files "app/src/main/java/**/MainActivity.kt" | head -n1 || true)"
[ -n "$MAIN" ] && [ -f "$MAIN" ] || MAIN="$(find app/src/main/java -type f -name MainActivity.kt -print -quit)"
[ -f "$MAIN" ] || { echo "❌ MainActivity.kt not found"; exit 1; }

# developer route
if ! grep -qE 'composable\("developer"\)' "$MAIN"; then
  perl -0777 -i -pe '
    s@(\s*composable\(\s*"news"\s*\)\s*\{[\s\S]*?\}\s*)@$1\n\n  composable("developer") {\n    val ctx = androidx.compose.ui.platform.LocalContext.current\n    androidx.compose.runtime.LaunchedEffect("open_dev") {\n      ctx.startActivity(android.content.Intent(ctx, com.secondmind.minimal.dev.DeveloperActivity::class.java))\n      nav.popBackStack()\n    }\n  }\n@' "$MAIN"
fi

# ai route
if ! grep -qE 'composable\("ai"\)' "$MAIN"; then
  perl -0777 -i -pe '
    s@(\s*composable\(\s*"news"\s*\)\s*\{[\s\S]*?\}\s*)@$1\n\n  composable("ai") {\n    val ctx = androidx.compose.ui.platform.LocalContext.current\n    androidx.compose.runtime.LaunchedEffect("open_ai") {\n      ctx.startActivity(android.content.Intent(ctx, com.secondmind.minimal.ai.AiChatActivity::class.java))\n      nav.popBackStack()\n    }\n  }\n@' "$MAIN"
fi

# Imports for MainActivity
insert_import_once "$MAIN" "import android.content.Intent"
insert_import_once "$MAIN" "import androidx.compose.runtime.LaunchedEffect"
insert_import_once "$MAIN" "import androidx.compose.ui.platform.LocalContext"

# --- 4) Patch DrawerContent to add AI item above Developer ---
DF="$(git ls-files "app/src/main/java/**/DrawerContent.kt" | head -n1 || true)"
if [ -z "$DF" ]; then
  DF="$(git grep -l -E "fun[[:space:]]+DrawerContent\\(" -- app/src/main/java || true | head -n1)"
fi

if [ -n "$DF" ] && [ -f "$DF" ]; then
  # Insert AI item before any line that references developer route
  if ! grep -qE 'onDestinationClicked\("ai"\)|navigate\("ai"\)|"AI"' "$DF"; then
    # try to place before developer item
    perl -0777 -i -pe '
      s@(.*onDestinationClicked\("developer"\).*?\n)@        NavigationDrawerItem(label = { Text("AI") }, selected = selectedRoute == "ai", onClick = { onDestinationClicked("ai") }, icon = { Icon(androidx.compose.material.icons.Icons.Filled.SmartToy, null) })\n\1@
    ' "$DF" || true
    # add imports (idempotent)
    insert_import_once "$DF" "import androidx.compose.material3.NavigationDrawerItem"
    insert_import_once "$DF" "import androidx.compose.material3.Text"
    insert_import_once "$DF" "import androidx.compose.material3.Icon"
    insert_import_once "$DF" "import androidx.compose.material.icons.Icons"
    insert_import_once "$DF" "import androidx.compose.material.icons.filled.SmartToy"
  fi
else
  echo "⚠️  DrawerContent.kt not found. Add an 'AI' item manually that navigates to route \"ai\" (it will launch the chat activity)."
fi

# --- 5) Stage & show summary ---
git add app/src/main/java/com/secondmind/minimal/dev/DeveloperActivity.kt \
        app/src/main/java/com/secondmind/minimal/ai/AiChatActivity.kt \
        "$MAN" "$MAIN" 2>/dev/null || true
[ -n "${DF:-}" ] && [ -f "$DF" ] && git add "$DF" || true

echo
echo "== Staged files =="
git diff --name-only --cached || true

echo
echo "== Preview (first 300 lines) =="
git --no-pager diff --cached | sed -n "1,300p" || true

echo
echo "✅ UI patch staged (no commit, no push). If build fails, re-run apply.sh (idempotent)."

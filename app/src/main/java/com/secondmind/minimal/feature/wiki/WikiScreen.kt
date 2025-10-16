package com.secondmind.minimal.feature.wiki

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WikiScreen(vm: WikiViewModel = viewModel(), modifier: Modifier = Modifier) {
  val ctx = LocalContext.current
  val bg = MaterialTheme.colorScheme.background
  var tts by remember { mutableStateOf<TextToSpeech?>(null) }
  DisposableEffect(Unit) {
    val engine = TextToSpeech(ctx) { s -> if (s == TextToSpeech.SUCCESS) try { tts?.language = Locale.getDefault() } catch (_: Throwable) {} }
    tts = engine
    onDispose { engine.stop(); engine.shutdown() }
  }

  var query by remember { mutableStateOf(TextFieldValue(vm.lastQuery)) }
  val state by vm.state.collectAsState()

  Surface(color = bg, modifier = modifier.fillMaxSize()) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
          modifier = Modifier.weight(1f),
          value = query,
          onValueChange = { query = it },
          singleLine = true,
          placeholder = { Text("Ask Wikipedia…") }
        )
        Button(enabled = !state.loading, onClick = { vm.ask(query.text) }) { Text(if (state.loading) "…" else "Search") }
        IconButton(onClick = { vm.random() }) { Icon(Icons.Filled.Refresh, contentDescription = "Random") }
      }

      if (state.error != null) {
        Text(state.error ?: "", color = MaterialTheme.colorScheme.error)
      }

      if (state.answer != null) {
        val a = state.answer!!
        Surface(
          shape = MaterialTheme.shapes.medium,
          color = MaterialTheme.colorScheme.surface,
          tonalElevation = 1.dp,
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
              Text(a.title, style = MaterialTheme.typography.titleMedium)
              IconButton(onClick = { tts?.speak(a.extract, TextToSpeech.QUEUE_FLUSH, null, "wiki-read") }) {
                Icon(Icons.Filled.VolumeUp, contentDescription = "Speak")
              }
            }
            if (a.thumbnail != null) {
              Image(
                painter = rememberAsyncImagePainter(a.thumbnail),
                contentDescription = "Thumb",
                modifier = Modifier.fillMaxWidth().height(140.dp),
                contentScale = ContentScale.Crop
              )
            }
            Text(a.extract)
          }
        }
      }

      if (state.related.isNotEmpty()) {
        Text("Related", style = MaterialTheme.typography.titleSmall)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          state.related.forEach { r ->
            AssistChip(onClick = { vm.ask(r) }, label = { Text(r) })
          }
        }
      }

      Spacer(Modifier.height(4.dp))
    }
  }
}

/** Simple FlowRow without extra deps */
@Composable
private fun FlowRow(
  modifier: Modifier = Modifier,
  horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
  verticalSpacing: Dp = 8.dp,
  content: @Composable () -> Unit
) {
  Row(modifier = modifier, horizontalArrangement = horizontalArrangement) { content() }
}

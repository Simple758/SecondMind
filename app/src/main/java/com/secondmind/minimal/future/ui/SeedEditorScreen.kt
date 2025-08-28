package com.secondmind.minimal.future.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.secondmind.minimal.future.db.*

@Composable
fun SeedEditorScreen(nav: NavHostController? = null) {
  val ctx = LocalContext.current
  val repo = remember { FutureRepos(ctx) }

  var text by remember { mutableStateOf("") }
  var isSeed by remember { mutableStateOf(true) }
  var type by remember { mutableStateOf("time") }
  var deadline by remember { mutableStateOf(System.currentTimeMillis() + 3600_000L) }
  var keyword by remember { mutableStateOf("") }
  var value by remember { mutableStateOf("") }

  Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Text("New Note / Seed", style = MaterialTheme.typography.titleLarge)

    OutlinedTextField(
      value = text, onValueChange = { text = it },
      label = { Text("Note") }, modifier = Modifier.fillMaxWidth()
    )

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      FilterChip(selected = isSeed, onClick = { isSeed = !isSeed }, label = { Text(if (isSeed) "Seed: ON" else "Seed: OFF") })
    }

    if (isSeed) {
      Text("Trigger type", style = MaterialTheme.typography.titleMedium)
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AssistChip(onClick = { type = "time" }, label = { Text("Time") }, leadingIcon = {})
        AssistChip(onClick = { type = "price_manual" }, label = { Text("Price (Manual)") }, leadingIcon = {})
        AssistChip(onClick = { type = "keyword" }, label = { Text("Keyword") }, leadingIcon = {})
      }
      if (type == "time") {
        OutlinedTextField(
          value = deadline.toString(),
          onValueChange = { runCatching { it.toLong() }.getOrNull()?.let { deadline = it } },
          label = { Text("Deadline (epoch ms)") },
          modifier = Modifier.fillMaxWidth()
        )
      } else if (type == "keyword") {
        OutlinedTextField(
          value = keyword, onValueChange = { keyword = it },
          label = { Text("Keyword") }, modifier = Modifier.fillMaxWidth()
        )
      } else {
        OutlinedTextField(
          value = value, onValueChange = { value = it },
          label = { Text("Target (manual)") }, modifier = Modifier.fillMaxWidth()
        )
      }
    }

    Spacer(Modifier.weight(1f))

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      Button(onClick = {
        // save
        if (text.isBlank()) return@Button
        // Note
        val noteId = kotlinx.coroutines.runBlocking { repo.notes.insert(NoteEntity(text = text, isSeed = isSeed)) }
        if (isSeed) {
          val s = SeedEntity(
            noteId = noteId,
            type = type,
            value = value,
            keyword = keyword,
            deadline = if (type == "time") deadline else 0L
          )
          kotlinx.coroutines.runBlocking { repo.seeds.insert(s) }
        }
        nav?.popBackStack()
      }) { Text("Save") }

      OutlinedButton(onClick = { nav?.popBackStack() }) { Text("Cancel") }
    }
  }
}

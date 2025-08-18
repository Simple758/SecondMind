package com.secondmind.minimal.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.secondmind.minimal.notes.QuickNoteStore

@Composable
fun QuickNoteCard(modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    var text by remember { mutableStateOf(QuickNoteStore.load(ctx)) }
    Card(modifier = modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Quick note", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = text,
                onValueChange = {
                    text = it
                    QuickNoteStore.save(ctx, it)
                },
                placeholder = { Text("Type something…") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 4
            )
        }
    }
}

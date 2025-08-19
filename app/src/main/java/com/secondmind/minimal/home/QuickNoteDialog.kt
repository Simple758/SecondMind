package com.secondmind.minimal.home

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import java.io.File

@Composable
fun QuickNoteDialog(
    show: Boolean,
    onDismiss: () -> Unit
) {
    if (!show) return
    val ctx = LocalContext.current
    val noteFile = remember { File(ctx.filesDir, "quick_note.txt") }
    var value by remember { mutableStateOf(TextFieldValue("")) }
    LaunchedEffect(Unit) {
        value = TextFieldValue(
            runCatching { noteFile.takeIf { it.exists() }?.readText().orEmpty() }
                .getOrElse { "" }
        )
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                runCatching { noteFile.writeText(value.text) }
                onDismiss()
            }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Quick note") },
        text = {
            Column(Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    singleLine = false,
                    minLines = 6,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}

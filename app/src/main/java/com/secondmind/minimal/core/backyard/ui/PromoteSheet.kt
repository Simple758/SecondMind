package com.secondmind.minimal.core.backyard.ui
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
/** Stub: user can promote Reception → Store Room after consent. */
@Composable
fun PromoteSheet(onDismiss: () -> Unit) {
  AlertDialog(
    onDismissRequest = onDismiss,
    confirmButton = { TextButton(onClick = onDismiss) { Text("OK") } },
    title = { Text("Promote to AI Store Room") },
    text = { Text("This is a stub. When implemented, you can curate and promote sanitized facts here.") },
    properties = DialogProperties(dismissOnClickOutside = true)
  )
}

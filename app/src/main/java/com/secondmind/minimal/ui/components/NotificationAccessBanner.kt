package com.secondmind.minimal.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.secondmind.minimal.util.NotificationAccess

@Composable
fun NotificationAccessBanner(modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    var enabled by remember { mutableStateOf(NotificationAccess.isEnabled(ctx)) }
    if (!enabled) {
        Card(modifier = modifier.fillMaxWidth()) {
            Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Notification access is off", style = MaterialTheme.typography.bodyMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { NotificationAccess.openSettings(ctx) }) { Text("Enable") }
                    OutlinedButton(onClick = { enabled = NotificationAccess.isEnabled(ctx) }) { Text("Refresh") }
                }
            }
        }
    }
}

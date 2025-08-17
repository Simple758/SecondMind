package com.secondmind.minimal.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InboxScreen(
    onOpenApps: (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Inbox", style = MaterialTheme.typography.headlineSmall)
        Text(
            "This is the Core build. Full inbox history & cleanup live in the Full flavor.",
            style = MaterialTheme.typography.bodyMedium
        )
        if (onOpenApps != null) {
            Button(onClick = onOpenApps) { Text("Open App Groups") }
        }
    }
}

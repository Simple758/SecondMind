package com.secondmind.minimal.v01.ui.pulse

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PulseScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Pulse — Top 3 Signals (stub)")
        Text("Whisper — non-repeating hint (stub)")
        Text("Quick actions: Add Note, Add Seed, Switch Mode (stubs)")
    }
}

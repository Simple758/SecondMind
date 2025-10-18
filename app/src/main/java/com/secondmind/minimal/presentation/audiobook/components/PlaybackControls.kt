// ============================================
// FILE: presentation/audiobook/components/PlaybackControls.kt
// ============================================

package com.secondmind.minimal.presentation.audiobook.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.secondmind.minimal.utils.OfflineAudioPlayer
import com.secondmind.minimal.utils.AudiobookConstants

@Composable
fun PlaybackControls(
    player: OfflineAudioPlayer,
    label: String
) {
    var position by remember { mutableStateOf(0L) }
    var duration by remember { mutableStateOf(1L) }
    var speed by remember { mutableStateOf(1.0f) }

    LaunchedEffect(Unit) {
        player.onProgress { pos, dur ->
            position = pos
            duration = if (dur <= 0) 1 else dur
        }
    }

    Column(Modifier.fillMaxWidth()) {
        Text(label, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { player.play() }) { Text("Play") }
            Button(onClick = { player.pause() }) { Text("Pause") }
        }
        Spacer(Modifier.height(8.dp))
        Slider(
            value = position.toFloat() / duration.toFloat(),
            onValueChange = { frac -> player.seekTo((frac * duration).toInt()) }
        )
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AudiobookConstants.SPEEDS.forEach { s ->
                FilterChip(
                    selected = (s == speed),
                    onClick = { speed = s; player.setSpeed(s) },
                    label = { Text("${s}x") }
                )
            }
        }
    }
}

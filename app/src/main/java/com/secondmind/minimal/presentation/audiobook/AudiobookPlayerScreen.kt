// ============================================
// FILE: presentation/audiobook/AudiobookPlayerScreen.kt
// ============================================

package com.secondmind.minimal.presentation.audiobook

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.secondmind.minimal.utils.OfflineAudioPlayer
import com.secondmind.minimal.presentation.audiobook.components.PlaybackControls
import com.secondmind.minimal.data.model.Audiobook

@Composable
fun AudiobookPlayerScreen(
    book: Audiobook
) {
    var selected by remember { mutableStateOf(0) }
    val player = remember { OfflineAudioPlayer() }
    val current = book.chapters.getOrNull(selected)

    LaunchedEffect(current?.audioFilePath) {
        current?.audioFilePath?.let { path ->
            player.load(path)
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(book.title, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))

        current?.let {
            PlaybackControls(player = player, label = it.title)
        }

        Spacer(Modifier.height(12.dp))

        Text("Chapters", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            itemsIndexed(book.chapters) { idx, ch ->
                ListItem(
                    headlineContent = { Text(ch.title) },
                    supportingContent = { Text("${ch.startPage}–${ch.endPage}") },
                    trailingContent = {
                        if (idx == selected) Text("•")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                )
                Divider()
            }
        }
    }
}

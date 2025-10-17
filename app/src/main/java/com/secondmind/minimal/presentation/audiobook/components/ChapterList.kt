// ============================================
// FILE: presentation/audiobook/components/ChapterList.kt
// ============================================

package com.secondmind.minimal.presentation.audiobook.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.secondmind.minimal.data.model.Chapter

@Composable
fun ChapterList(chapters: List<Chapter>) {
    LazyColumn {
        items(chapters) { ch ->
            ListItem(
                headlineContent = { Text(ch.title) },
                supportingContent = { Text("${ch.startPage}–${ch.endPage}") },
                overlineContent = { if (!ch.summary.isNullOrBlank()) Text(ch.summary!!) },
                modifier = Modifier.fillMaxWidth()
            )
            Divider()
        }
    }
}

// ============================================
// FILE: presentation/audiobook/components/BookCoverCard.kt
// ============================================

package com.secondmind.minimal.presentation.audiobook.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.secondmind.minimal.data.model.Audiobook

@Composable
fun BookCoverCard(book: Audiobook) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(book.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            book.author?.let {
                Spacer(Modifier.height(4.dp))
                Text("by $it", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(Modifier.height(8.dp))
            Text("Chapters: ${book.chapters.size}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

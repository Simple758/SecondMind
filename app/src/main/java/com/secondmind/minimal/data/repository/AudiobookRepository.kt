// ============================================
// FILE: data/repository/AudiobookRepository.kt
// ============================================

package com.secondmind.minimal.data.repository

import android.content.Context
import android.net.Uri
import com.secondmind.minimal.data.model.Audiobook
import com.secondmind.minimal.data.model.Chapter
import kotlinx.coroutines.flow.Flow

interface AudiobookRepository {
    suspend fun importPdf(context: Context, uri: Uri): Audiobook
    suspend fun summarizeChapters(context: Context, book: Audiobook): Audiobook
    suspend fun generateAudioForChapters(context: Context, book: Audiobook): Audiobook

    fun listCached(): Flow<List<Audiobook>>
    suspend fun clearBook(context: Context, bookId: String)
}

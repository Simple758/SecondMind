// ============================================
// FILE: data/repository/AudiobookRepositoryImpl.kt
// ============================================

package com.secondmind.minimal.data.repository

@file:Suppress("UNUSED_IMPORT")

import android.content.Context
import android.net.Uri
import com.secondmind.minimal.data.model.Audiobook
import com.secondmind.minimal.data.model.Chapter
import com.secondmind.minimal.data.source.PdfExtractor
import com.secondmind.minimal.data.source.AudiobookCacheManager
import com.secondmind.minimal.utils.AudiobookConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File

// DeepSeek integration (assuming these exist in your project under this package)
import com.secondmind.minimal.ai.AIServiceLocator
import com.secondmind.minimal.ai.AIResult
import com.secondmind.minimal.ai.ContextPacket
import com.secondmind.minimal.ai.Prompt
import com.secondmind.minimal.ai.system
import com.secondmind.minimal.ai.user
import com.secondmind.minimal.ai.options
import com.secondmind.minimal.ai.AIOptions

class AudiobookRepositoryImpl(
    private val extractor: PdfExtractor = PdfExtractor(),
    private val cache: AudiobookCacheManager = AudiobookCacheManager()
) : AudiobookRepository {

    private val cacheFlow = MutableStateFlow<List<Audiobook>>(emptyList())

    override fun listCached(): Flow<List<Audiobook>> = cacheFlow.asStateFlow()

    override suspend fun importPdf(context: Context, uri: Uri): Audiobook = withContext(Dispatchers.IO) {
        val book = extractor.extractToAudiobook(context, uri)
        // Update cache listing (in-memory for now)
        cacheFlow.value = (cacheFlow.value + book).distinctBy { it.id }
        book
    }

    override suspend fun summarizeChapters(context: Context, book: Audiobook): Audiobook = withContext(Dispatchers.IO) {
        val updated = book.chapters.map { ch ->
            val summary = requestSummary(context, book.title, ch.title, ch.text.take(AudiobookConstants.SUMMARY_TEXT_LIMIT))
            ch.copy(summary = summary)
        }
        book.copy(chapters = updated)
    }

    override suspend fun generateAudioForChapters(context: Context, book: Audiobook): Audiobook = withContext(Dispatchers.IO) {
        val dir = cache.bookDir(context, book.id)
        var totalDuration = 0L
        val updated = book.chapters.mapIndexed { idx, ch ->
            val out = File(dir, "ch_${idx+1}.wav")
            val bytes = cache.synthesizeToWav(context, ch.text, out)
            // Very rough estimation placeholder: bytes/16 ~= ms (depends on TTS output; adjust if needed)
            val durationGuess = bytes / 16
            totalDuration += durationGuess
            ch.copy(audioFilePath = out.absolutePath, durationMs = durationGuess)
        }
        val newBook = book.copy(chapters = updated, totalDurationMs = totalDuration)
        // Update listing
        cacheFlow.value = cacheFlow.value.map { if (it.id == book.id) newBook else it }
        newBook
    }

    private suspend fun requestSummary(context: Context, bookTitle: String, chapterTitle: String, textHint: String): String {
        return try {
            val result = AIServiceLocator.get().complete(
                context = context,
                prompt = Prompt(
                    system("You are a helpful literary assistant. Provide a concise 3-bullet summary."),
                    user("Summarize '$chapterTitle' of '$bookTitle' in 3 bullet points. Focus on key plot, themes, and characters."),
                    options(AIOptions(
                        model = "deepseek-chat",
                        maxTokens = 400,
                        temperature = 0.3,
                        packet = ContextPacket(
                            source = ContextPacket.Source.UI,
                            appPackage = context.packageName,
                            text = "chapter summary request"
                        )
                    ))
                )
            )
            when (result) {
                is AIResult.Text -> result.content
                is AIResult.Error -> "Error: ${result.message}"
                else -> "No result"
            }
        } catch (e: Throwable) {
            "AI unavailable: ${e.message}"
        }
    }

    override suspend fun clearBook(context: Context, bookId: String) {
        cache.clearBook(context, bookId)
        cacheFlow.value = cacheFlow.value.filterNot { it.id == bookId }
    }
}

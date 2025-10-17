// ============================================
// FILE: data/source/AudiobookCacheManager.kt
// ============================================

package com.secondmind.minimal.data.source

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Manages audiobook cache directory and TTS file synthesis.
 * Uses TextToSpeech.synthesizeToFile for OFFLINE-first generation (device voices).
 */
class AudiobookCacheManager {

    fun cacheRoot(context: Context): File =
        File(context.getExternalFilesDir(null), "audiobooks").apply { mkdirs() }

    fun bookDir(context: Context, bookId: String): File =
        File(cacheRoot(context), bookId).apply { mkdirs() }

    suspend fun synthesizeToWav(
        context: Context,
        text: String,
        outFile: File,
        locale: Locale = Locale.getDefault()
    ): Long = suspendCancellableCoroutine { cont ->
        val tts = TextToSpeech(context) { status ->
            if (status != TextToSpeech.SUCCESS) {
                cont.resumeWithException(IllegalStateException("TTS init failed: $status"))
                return@TextToSpeech
            }
            tts.language = locale
            tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}
                override fun onError(utteranceId: String?) {
                    if (cont.isActive) cont.resumeWithException(IllegalStateException("TTS error"))
                    tts.shutdown()
                }
                override fun onDone(utteranceId: String?) {
                    if (cont.isActive) cont.resume(outFile.length())
                    tts.shutdown()
                }
            })

            // Params
            val params = HashMap<String, String>()
            params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "utter_" + System.currentTimeMillis()

            val res = tts.synthesizeToFile(text, params, outFile, params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID])
            if (res != TextToSpeech.SUCCESS) {
                if (cont.isActive) cont.resumeWithException(IllegalStateException("synthesizeToFile failed: $res"))
                tts.shutdown()
            }
        }

        cont.invokeOnCancellation {
            try { tts.shutdown() } catch (_: Throwable) {}
        }
    }

    fun clearBook(context: Context, bookId: String) {
        bookDir(context, bookId).deleteRecursively()
    }
}

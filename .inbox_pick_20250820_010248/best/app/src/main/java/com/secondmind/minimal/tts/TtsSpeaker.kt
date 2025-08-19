package com.secondmind.minimal.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

object TtsSpeaker {
    @Volatile private var tts: TextToSpeech? = null
    @Volatile private var ready: Boolean = false
    @Volatile private var initTried: Boolean = false

    private fun ensure(ctx: Context) {
        if (ready && tts != null) return
        if (!initTried) {
            initTried = true
            tts = TextToSpeech(ctx.applicationContext) { status ->
                ready = (status == TextToSpeech.SUCCESS).also {
                    if (it) {
                        tts?.language = Locale.getDefault()
                        tts?.setPitch(1.0f)
                        tts?.setSpeechRate(1.0f)
                    }
                }
            }
        }
    }

    @Synchronized
    fun speak(ctx: Context, text: String) {
        if (text.isBlank()) return
        ensure(ctx)
        val engine = tts ?: return
        // Keep IDs unique and short
        engine.speak(text.take(500), TextToSpeech.QUEUE_ADD, null, "sm-${System.currentTimeMillis() and 0xFFFF}")
    }
}

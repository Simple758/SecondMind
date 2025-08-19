package com.secondmind.minimal.tts

import android.content.Context
import android.speech.tts.TextToSpeech

class TtsHelper(ctx: Context) {
    private val tts = TextToSpeech(ctx) { /* ignore init result */ }

    fun speak(text: String) {
        if (text.isBlank()) return
        try {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "sm-${System.currentTimeMillis()}")
        } catch (_: Throwable) { /* ignore */ }
    }

    fun shutdown() {
        try { tts.shutdown() } catch (_: Throwable) { /* ignore */ }
    }
}

package com.secondmind.minimal.util

import android.content.Context
import android.speech.tts.TextToSpeech

class TtsSpeaker(ctx: Context) {
    private val tts = TextToSpeech(ctx) { /* ignore init result */ }
    fun speak(text: String) {
        if (text.isBlank()) return
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "inbox")
    }
    fun shutdown() = tts.shutdown()
}

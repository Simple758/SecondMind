package com.secondmind.minimal.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TtsSpeaker(ctx: Context) {
    private var tts: TextToSpeech? = null

    init {
        val appCtx = ctx.applicationContext
        tts = TextToSpeech(appCtx) { res ->
            if (res == TextToSpeech.SUCCESS) {
                try { tts?.language = Locale.getDefault() } catch (_: Throwable) {}
            }
        }
    }

    fun speak(text: String) {
        if (text.isBlank()) return
        try { tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "sm-a1") } catch (_: Throwable) {}
    }

    fun shutdown() {
        try { tts?.shutdown() } catch (_: Throwable) {}
    }
}

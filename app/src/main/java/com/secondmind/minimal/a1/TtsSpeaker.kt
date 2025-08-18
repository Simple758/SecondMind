package com.secondmind.minimal.a1

import android.content.Context
import android.speech.tts.TextToSpeech

class TtsSpeaker(ctx: Context) {
  private val tts = TextToSpeech(ctx) { /* ignore result */ }
  fun speak(text: String) {
    if (text.isBlank()) return
    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "a1")
  }
  fun shutdown() = tts.shutdown()
}

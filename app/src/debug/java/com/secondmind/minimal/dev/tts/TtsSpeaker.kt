package com.secondmind.minimal.dev.tts

import android.content.Context
import android.speech.tts.TextToSpeech

class TtsSpeaker(ctx: Context) {
  private val tts = TextToSpeech(ctx) { }
  fun speak(text: String) {
    if (text.isBlank()) return
    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "patch")
  }
  fun shutdown() = tts.shutdown()
}

package com.secondmind.minimal

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

object Tts {
  @Volatile private var tts: TextToSpeech? = null

  fun init(context: Context) {
    if (tts != null) return
    tts = TextToSpeech(context.applicationContext) { status ->
      if (status == TextToSpeech.SUCCESS) tts?.language = Locale.getDefault()
    }
  }
  fun speak(text: String) {
    val t = tts ?: return
    if (text.isBlank()) return
    t.speak(text, TextToSpeech.QUEUE_ADD, null, "sm-${System.currentTimeMillis()}")
  }
  fun shutdown() { tts?.shutdown(); tts = null }
}

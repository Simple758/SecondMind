package com.secondmind.minimal.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean

object Reader {
  @Volatile private var tts: TextToSpeech? = null
  private val ready = AtomicBoolean(false)

  private fun ensure(ctx: Context) {
    if (tts == null) {
      synchronized(this) {
        if (tts == null) {
          tts = TextToSpeech(ctx.applicationContext) { status ->
            ready.set(status == TextToSpeech.SUCCESS)
            if (ready.get()) tts?.language = Locale.getDefault()
          }
        }
      }
    }
  }

  fun speak(ctx: Context, text: String) {
    if (text.isBlank()) return
    ensure(ctx)
    val engine = tts ?: return
    if (!ready.get()) return
    engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "secondmind_read")
  }

  fun shutdown() {
    tts?.shutdown()
    tts = null
    ready.set(false)
  }
}

package com.secondmind.minimal.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean

object Reader {
  @Volatile private var tts: TextToSpeech? = null
  private val ready = AtomicBoolean(false)
  @Volatile private var enabled: Boolean = true
  @Volatile private var rate: Float = 1.0f
  @Volatile private var pitch: Float = 1.0f

  private fun ensure(ctx: Context) {
    if (tts == null) {
      synchronized(this) {
        if (tts == null) {
          tts = TextToSpeech(ctx.applicationContext) { status ->
            ready.set(status == TextToSpeech.SUCCESS)
            if (ready.get()) {
              tts?.language = Locale.getDefault()
              tts?.setSpeechRate(rate)
              tts?.setPitch(pitch)
            }
          }
        }
      }
    }
  }

  fun updateConfig(enabled: Boolean, rate: Float, pitch: Float, ctx: Context? = null) {
    this.enabled = enabled
    this.rate = rate.coerceIn(0.5f, 1.5f)
    this.pitch = pitch.coerceIn(0.5f, 1.5f)
    if (ctx != null) ensure(ctx)
    tts?.setSpeechRate(this.rate)
    tts?.setPitch(this.pitch)
  }

  fun speak(ctx: Context, text: String) {
    if (!enabled) return
    val msg = text.trim()
    if (msg.isEmpty()) return
    ensure(ctx)
    val engine = tts ?: return
    if (!ready.get()) return
    engine.setSpeechRate(rate)
    engine.setPitch(pitch)
    engine.speak(msg, TextToSpeech.QUEUE_FLUSH, null, "secondmind_read")
  }

  fun shutdown() {
    tts?.shutdown()
    tts = null
    ready.set(false)
  }
}

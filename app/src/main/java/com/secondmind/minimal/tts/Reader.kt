package com.secondmind.minimal.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.concurrent.atomic.AtomicBoolean

object Reader {
  @Volatile private var tts: TextToSpeech? = null
  private val ready = AtomicBoolean(false)
  @Volatile private var enabled: Boolean = true
  @Volatile private var rate: Float = 1.0f
  @Volatile private var pitch: Float = 1.0f
  @Volatile private var voiceName: String? = null

  private fun ensure(ctx: Context) {
    if (tts != null) return
    try {
      val google = "com.google.android.tts"
      val engine = try { ctx.packageManager.getPackageInfo(google, 0); google } catch (_: Throwable) { null }
      tts = if (engine != null)
        TextToSpeech(ctx.applicationContext, { status ->
          ready.set(status == TextToSpeech.SUCCESS)
          if (ready.get()) applyParams()
        }, engine)
      else
        TextToSpeech(ctx.applicationContext) { status ->
          ready.set(status == TextToSpeech.SUCCESS)
          if (ready.get()) applyParams()
        }
    } catch (_: Throwable) {
      tts = null
      ready.set(false)
    }
  }

  @Synchronized private fun applyParams() {
    val e = tts ?: return
    try {
      e.setSpeechRate(rate)
      e.setPitch(pitch)
      val vname = voiceName
      if (!vname.isNullOrBlank()) {
        val all = try { e.voices?.toList() ?: emptyList() } catch (_: Throwable) { emptyList() }
        val match = all.firstOrNull { it.name == vname }
        if (match != null) {
          try { e.setVoice(match) } catch (_: Throwable) {}
        }
      }
    } catch (_: Throwable) {}
  }

  fun updateConfig(enabled: Boolean, rate: Float, pitch: Float, ctx: Context? = null) {
    this.enabled = enabled
    this.rate = rate.coerceIn(0.5f, 2.0f)
    this.pitch = pitch.coerceIn(0.5f, 2.0f)
    if (ctx != null) ensure(ctx)
    applyParams()
  }

  fun updateVoice(voiceName: String?, ctx: Context? = null) {
    this.voiceName = voiceName
    if (ctx != null) ensure(ctx)
    applyParams()
  }

  fun speak(ctx: Context, msg: String) {
    if (!enabled) return
    if (msg.isBlank()) return
    ensure(ctx)
    val e = tts ?: return
    if (!ready.get()) return
    applyParams()
    try {
      e.speak(msg.take(500), TextToSpeech.QUEUE_FLUSH, null, "secondmind_read")
    } catch (_: Throwable) {}
  }

  fun stop() {
    try { tts?.stop() } catch (_: Throwable) {}
  }

  fun shutdown() {
    try { tts?.shutdown() } catch (_: Throwable) {}
    tts = null
    ready.set(false)
  }
}

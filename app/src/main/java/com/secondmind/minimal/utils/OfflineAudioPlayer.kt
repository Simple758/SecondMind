// ============================================
// FILE: utils/OfflineAudioPlayer.kt
// ============================================

package com.secondmind.minimal.utils

import android.media.MediaPlayer
import android.media.PlaybackParams
import java.io.File

/**
 * Simple wrapper around MediaPlayer for offline WAV playback with speed control (API 23+).
 */
class OfflineAudioPlayer {

    private var mp: MediaPlayer? = null

    // Progress callback: (positionMs, durationMs)
    var onProgress: ((Long, Long) -> Unit)? = null

    fun load(path: String) {
        release()
        val file = File(path)
        val p = MediaPlayer()
        p.setDataSource(file.absolutePath)
        p.setOnPreparedListener { startProgress() }
        p.prepareAsync()
        mp = p
    }

    private fun startProgress() {
        // Very light polling loop
        Thread {
            while (mp != null) {
                try {
                    val cur = mp!!.currentPosition.toLong()
                    val dur = mp!!.duration.toLong()
                    onProgress?.invoke(cur, dur)
                    Thread.sleep(300)
                } catch (_: Throwable) { break }
            }
        }.start()
    }

    fun play() {
        mp?.start()
    }

    fun pause() {
        mp?.pause()
    }

    fun seekTo(ms: Long) {
        mp?.seekTo(ms.toInt())
    }

    fun setSpeed(speed: Float) {
        try {
            val p = mp?.playbackParams ?: PlaybackParams()
            p.speed = speed
            mp?.playbackParams = p
        } catch (_: Throwable) {
            // ignore if not supported
        }
    }

    fun release() {
        mp?.stop()
        mp?.release()
        mp = null
    }
}

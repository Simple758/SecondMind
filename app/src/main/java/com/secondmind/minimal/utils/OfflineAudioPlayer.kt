package com.secondmind.minimal.utils

import android.media.MediaPlayer
import android.media.PlaybackParams
import android.os.Build
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.os.Handler
import android.os.Looper

class OfflineAudioPlayer {
    private var player: MediaPlayer? = null
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying
    private val handler = Handler(Looper.getMainLooper())
    private var progressCallback: ((Long, Long) -> Unit)? = null

    fun load(path: String) {
        player?.release()
        player = MediaPlayer().apply {
            setDataSource(path)
            prepare()
            setOnCompletionListener { 
                _isPlaying.value = false
                stopProgressUpdates()
            }
        }
    }

    fun play() {
        player?.start()
        _isPlaying.value = true
        startProgressUpdates()
    }

    fun pause() {
        player?.pause()
        _isPlaying.value = false
        stopProgressUpdates()
    }

    fun seekBy(ms: Long) {
        player?.let {
            val newPos = (it.currentPosition + ms).coerceIn(0, it.duration)
            it.seekTo(newPos.toInt())
        }
    }

    fun seekTo(position: Int) {
        player?.seekTo(position)
    }

    fun setSpeed(speed: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            player?.playbackParams = player?.playbackParams?.setSpeed(speed) ?: PlaybackParams().setSpeed(speed)
        }
    }

    fun onProgress(callback: (position: Long, duration: Long) -> Unit) {
        progressCallback = callback
    }

    private fun startProgressUpdates() {
        handler.post(object : Runnable {
            override fun run() {
                player?.let {
                    progressCallback?.invoke(it.currentPosition.toLong(), it.duration.toLong())
                }
                if (_isPlaying.value) {
                    handler.postDelayed(this, 100)
                }
            }
        })
    }

    private fun stopProgressUpdates() {
        handler.removeCallbacksAndMessages(null)
    }

    fun release() {
        stopProgressUpdates()
        player?.release()
        player = null
        _isPlaying.value = false
    }
}

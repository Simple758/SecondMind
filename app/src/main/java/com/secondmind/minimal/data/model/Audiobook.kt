// ============================================
// FILE: data/model/Audiobook.kt
// ============================================

package com.secondmind.minimal.data.model

import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Core audiobook data structures.
 */
data class Audiobook(
    val id: String,
    val title: String,
    val author: String? = null,
    val coverUri: Uri? = null,
    val chapters: List<Chapter> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val totalDurationMs: Long = 0L
)

data class Chapter(
    val index: Int,
    val title: String,
    val startPage: Int,
    val endPage: Int,
    val text: String,
    val summary: String? = null,
    val audioFilePath: String? = null,
    val durationMs: Long = 0L,
    val progressMs: Long = 0L
)

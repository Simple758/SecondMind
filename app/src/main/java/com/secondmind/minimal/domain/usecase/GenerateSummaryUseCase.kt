// ============================================
// FILE: domain/usecase/GenerateSummaryUseCase.kt
// ============================================

package com.secondmind.minimal.domain.usecase

import android.content.Context
import com.secondmind.minimal.data.model.Audiobook
import com.secondmind.minimal.data.repository.AudiobookRepository

class GenerateSummaryUseCase(private val repo: AudiobookRepository) {
    suspend operator fun invoke(context: Context, book: Audiobook): Audiobook =
        repo.summarizeChapters(context, book)
}

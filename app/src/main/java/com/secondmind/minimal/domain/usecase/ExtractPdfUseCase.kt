// ============================================
// FILE: domain/usecase/ExtractPdfUseCase.kt
// ============================================

package com.secondmind.minimal.domain.usecase

import android.content.Context
import android.net.Uri
import com.secondmind.minimal.data.model.Audiobook
import com.secondmind.minimal.data.repository.AudiobookRepository

class ExtractPdfUseCase(private val repo: AudiobookRepository) {
    suspend operator fun invoke(context: Context, uri: Uri): Audiobook =
        repo.importPdf(context, uri)
}

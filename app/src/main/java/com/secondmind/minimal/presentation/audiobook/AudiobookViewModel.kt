// ============================================
// FILE: presentation/audiobook/AudiobookViewModel.kt
// ============================================

package com.secondmind.minimal.presentation.audiobook

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.secondmind.minimal.data.model.Audiobook
import com.secondmind.minimal.data.repository.AudiobookRepository
import com.secondmind.minimal.data.repository.AudiobookRepositoryImpl
import com.secondmind.minimal.domain.usecase.ExtractPdfUseCase
import com.secondmind.minimal.domain.usecase.GenerateAudioUseCase
import com.secondmind.minimal.domain.usecase.GenerateSummaryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AudiobookUiState(
    val current: Audiobook? = null,
    val isBusy: Boolean = false,
    val message: String? = null,
    val progress: Float = 0f
)

class AudiobookViewModel(
    private val repository: AudiobookRepository = AudiobookRepositoryImpl()
) : ViewModel() {

    private val extract = ExtractPdfUseCase(repository)
    private val summarize = GenerateSummaryUseCase(repository)
    private val generateAudio = GenerateAudioUseCase(repository)

    private val _state = MutableStateFlow(AudiobookUiState())
    val state: StateFlow<AudiobookUiState> = _state

    fun importPdf(context: Context, uri: Uri) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isBusy = true, message = "Importing PDF...")
            val book = extract(context, uri)
            _state.value = _state.value.copy(current = book, message = "Generating summaries...")
            val withSummaries = summarize(context, book)
            _state.value = _state.value.copy(current = withSummaries, message = "Generating audio files...")
            val withAudio = generateAudio(context, withSummaries)
            _state.value = _state.value.copy(current = withAudio, isBusy = false, message = "Ready")
        }
    }
}

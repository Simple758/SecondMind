package com.secondmind.minimal.feature.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class NewsUiState(
    val tab: NewsTab = NewsTab.ForYou,
    val loading: Boolean = false,
    val articles: List<Article> = emptyList(),
    val error: String? = null
)

class NewsViewModel: ViewModel() {
    private val _state = MutableStateFlow(NewsUiState())
    val state: StateFlow<NewsUiState> = _state

    fun setTab(tab: NewsTab) {
        if (tab == _state.value.tab) return
        _state.value = _state.value.copy(tab = tab)
        refresh(false)
    }

    fun refresh(force: Boolean = true) {
        val tab = _state.value.tab
        _state.value = _state.value.copy(loading = true, error = null)
        viewModelScope.launch {
            try {
                val items = NewsRepository.get(tab, force)
                _state.value = _state.value.copy(loading = false, articles = items, error = null)
            } catch (t: Throwable) {
                _state.value = _state.value.copy(loading = false, error = t.message ?: "error")
            }
        }
    }
}

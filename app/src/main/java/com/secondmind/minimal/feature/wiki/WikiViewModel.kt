package com.secondmind.minimal.feature.wiki

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

data class UiState(
  val loading: Boolean = false,
  val answer: UiAnswer? = null,
  val related: List<String> = emptyList(),
  val error: String? = null
)
data class UiAnswer(val title: String, val extract: String, val thumbnail: String?)

class WikiViewModel(app: Application) : AndroidViewModel(app) {
  private val _state = MutableStateFlow(UiState())
  val state = _state.asStateFlow()
  var lastQuery: String = "" ; private set

  fun ask(q: String) {
    val query = q.trim()
    if (query.isEmpty()) return
    lastQuery = query
    val ctx = getApplication<Application>()
    _state.value = _state.value.copy(loading = true, error = null)
    viewModelScope.launch {
      try {
        withContext(Dispatchers.IO) {
          // cache by title after resolving
          val titles = searchTitles(query, limit = 5)
          if (titles.isEmpty()) {
            _state.value = UiState(loading = false, error = "No Wikipedia results for '$query'. Try different keywords.", answer = null, related = emptyList())
            return@withContext
          }
          val title = titles.firstOrNull() ?: query
          android.util.Log.d("WikiViewModel", "Searching for: $query -> Found titles: $titles -> Using: $title")
          val cacheKey = "sum:$title"
          val cached = cacheGet(ctx, cacheKey)
          val summary = if (cached != null) WikiSummary(title, cached, null) else getSummary(title)

          if (summary != null) {
            cachePut(ctx, cacheKey, summary.extract, 7)
            val rel = (titles.drop(1) + relatedFrom(summary)).distinct().take(6)
            _state.value = UiState(
              loading = false,
              answer = UiAnswer(summary.title, summary.extract, summary.thumbnail),
              related = rel,
              error = null
            )
          } else {
            _state.value = UiState(loading = false, error = "Wikipedia returned no summary for '$title'. Article may not exist.", answer = null, related = emptyList())
          }
        }
      } catch (t: Throwable) {
        _state.value = UiState(loading = false, error = t.message ?: "Failed", answer = null, related = emptyList())
      }
    }
  }

  fun random() = ask("Special:Random")
  private fun relatedFrom(sum: WikiSummary): List<String> {
    val txt = sum.extract
    val candidates = txt.split(" ").filter { it.length > 6 && it[0].isUpperCase() }
    return candidates.take(3)
  }
}

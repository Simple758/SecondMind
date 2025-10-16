mport kotlinx.coroutines.Dispatchers
package com.secondmind.minimal.feature.wiki
mport kotlinx.coroutines.Dispatchers

mport kotlinx.coroutines.Dispatchers
import android.app.Application
mport kotlinx.coroutines.Dispatchers
import androidx.lifecycle.AndroidViewModel
mport kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewModelScope
mport kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
mport kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
mport kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
mport kotlinx.coroutines.Dispatchers

mport kotlinx.coroutines.Dispatchers
class WikiViewModel(app: Application) : AndroidViewModel(app) {
mport kotlinx.coroutines.Dispatchers
  data class UiAnswer(val title: String, val extract: String, val thumbnail: String?)
mport kotlinx.coroutines.Dispatchers
  data class UiState(
mport kotlinx.coroutines.Dispatchers
    val loading: Boolean = false,
mport kotlinx.coroutines.Dispatchers
    val answer: UiAnswer? = null,
mport kotlinx.coroutines.Dispatchers
    val related: List<String> = emptyList(),
mport kotlinx.coroutines.Dispatchers
    val error: String? = null
mport kotlinx.coroutines.Dispatchers
  )
mport kotlinx.coroutines.Dispatchers

mport kotlinx.coroutines.Dispatchers
  private val _state = MutableStateFlow(UiState())
mport kotlinx.coroutines.Dispatchers
  val state = _state.asStateFlow()
mport kotlinx.coroutines.Dispatchers
  var lastQuery: String = "" ; private set
mport kotlinx.coroutines.Dispatchers

mport kotlinx.coroutines.Dispatchers
  fun ask(q: String) {
mport kotlinx.coroutines.Dispatchers
    val query = q.trim()
mport kotlinx.coroutines.Dispatchers
    if (query.isEmpty()) return
mport kotlinx.coroutines.Dispatchers
    lastQuery = query
mport kotlinx.coroutines.Dispatchers
    val ctx = getApplication<Application>()
mport kotlinx.coroutines.Dispatchers
    _state.value = _state.value.copy(loading = true, error = null)
mport kotlinx.coroutines.Dispatchers
    viewModelScope.launch {
mport kotlinx.coroutines.Dispatchers
      try {
mport kotlinx.coroutines.Dispatchers
        withContext(Dispatchers.IO) {
mport kotlinx.coroutines.Dispatchers
          // cache by title after resolving
mport kotlinx.coroutines.Dispatchers
          val titles = searchTitles(query, limit = 5)
mport kotlinx.coroutines.Dispatchers
          if (titles.isEmpty()) {
mport kotlinx.coroutines.Dispatchers
            _state.value = UiState(loading = false, error = "No Wikipedia results for '$query'. Try different keywords.", answer = null, related = emptyList())
mport kotlinx.coroutines.Dispatchers
            return@withContext
mport kotlinx.coroutines.Dispatchers
          }
mport kotlinx.coroutines.Dispatchers
          val title = titles.firstOrNull() ?: query
mport kotlinx.coroutines.Dispatchers
          android.util.Log.d("WikiViewModel", "Searching for: $query -> Found titles: $titles -> Using: $title")
mport kotlinx.coroutines.Dispatchers
          val cacheKey = "sum:$title"
mport kotlinx.coroutines.Dispatchers
          val cached = cacheGet(ctx, cacheKey)
mport kotlinx.coroutines.Dispatchers
          val summary = if (cached != null) WikiSummary(title, cached, null) else getSummary(title)
mport kotlinx.coroutines.Dispatchers

mport kotlinx.coroutines.Dispatchers
          if (summary != null) {
mport kotlinx.coroutines.Dispatchers
            cachePut(ctx, cacheKey, summary.extract, 7)
mport kotlinx.coroutines.Dispatchers
            val rel = (titles.drop(1) + relatedFrom(summary)).distinct().take(6)
mport kotlinx.coroutines.Dispatchers
            _state.value = UiState(
mport kotlinx.coroutines.Dispatchers
              loading = false,
mport kotlinx.coroutines.Dispatchers
              answer = UiAnswer(summary.title, summary.extract, summary.thumbnail),
mport kotlinx.coroutines.Dispatchers
              related = rel,
mport kotlinx.coroutines.Dispatchers
              error = null
mport kotlinx.coroutines.Dispatchers
            )
mport kotlinx.coroutines.Dispatchers
          } else {
mport kotlinx.coroutines.Dispatchers
            _state.value = UiState(loading = false, error = "Wikipedia returned no summary for '$title'. Article may not exist.", answer = null, related = emptyList())
mport kotlinx.coroutines.Dispatchers
          }
mport kotlinx.coroutines.Dispatchers
        }
mport kotlinx.coroutines.Dispatchers
      } catch (t: Throwable) {
mport kotlinx.coroutines.Dispatchers
        _state.value = UiState(loading = false, error = t.message ?: "Failed", answer = null, related = emptyList())
mport kotlinx.coroutines.Dispatchers
      }
mport kotlinx.coroutines.Dispatchers
    }
mport kotlinx.coroutines.Dispatchers
  }
mport kotlinx.coroutines.Dispatchers

mport kotlinx.coroutines.Dispatchers
  fun random() {
mport kotlinx.coroutines.Dispatchers
    ask("Special:Random")
mport kotlinx.coroutines.Dispatchers
  }
mport kotlinx.coroutines.Dispatchers

mport kotlinx.coroutines.Dispatchers
  private fun relatedFrom(s: WikiSummary): List<String> {
mport kotlinx.coroutines.Dispatchers
    // naive: split by sentences and pick keywords
mport kotlinx.coroutines.Dispatchers
    val txt = s.extract
mport kotlinx.coroutines.Dispatchers
    val words = txt.split(Regex("""\W+""")).map { it.trim() }.filter { it.length in 4..16 }
mport kotlinx.coroutines.Dispatchers
    return words.groupingBy { it }.eachCount().toList().sortedByDescending { it.second }.map { it.first }.distinct().take(6)
mport kotlinx.coroutines.Dispatchers
  }
mport kotlinx.coroutines.Dispatchers
}

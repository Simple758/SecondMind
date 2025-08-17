package com.secondmind.minimal.inbox

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class InboxItem(val id: Long = 0, val title: String = "", val text: String = "", val app: String = "")

class InboxViewModel : ViewModel() {
  private val _items = MutableStateFlow<List<InboxItem>>(emptyList())
  val items: StateFlow<List<InboxItem>> = _items
}

package com.secondmind.minimal.inbox

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.secondmind.minimal.data.AppDb
import com.secondmind.minimal.data.InboxRepository
import com.secondmind.minimal.data.NotificationEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InboxViewModel(private val app: Application) : ViewModel() {
  private val repo = InboxRepository(AppDb.get(app).notificationDao())
  val notifications: StateFlow<List<NotificationEntity>> =
    repo.stream().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  fun delete(id: Long) = viewModelScope.launch { repo.delete(id) }
  fun clear() = viewModelScope.launch { repo.clear() }

  companion object {
    fun factory(app: Application): ViewModelProvider.Factory =
      object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
          return InboxViewModel(app) as T
        }
      }
  }
}

package com.secondmind.minimal.a1

import android.app.Notification
import android.app.Application
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Live bus of active notifications mirrored from the system
object A1NotifBus {
  val active = MutableSharedFlow<List<StatusBarNotification>>(replay = 1, extraBufferCapacity = 64)
}

class A1NotificationMirrorService : NotificationListenerService() {
  private fun publish() {
    val list = try { activeNotifications?.toList().orEmpty() } catch (_: Throwable) { emptyList() }
    A1NotifBus.active.tryEmit(list)
  }
  override fun onListenerConnected() = publish()
  override fun onNotificationPosted(sbn: StatusBarNotification) = publish()
  override fun onNotificationRemoved(sbn: StatusBarNotification) = publish()
}

data class A1Msg(val key: String, val pkg: String, val title: String, val text: String, val ts: Long)
data class A1Group(val pkg: String, val appLabel: String, val messages: List<A1Msg>)

class A1ViewModel(app: Application) : AndroidViewModel(app) {
  private val pm = app.packageManager
  private val _groups = MutableStateFlow<List<A1Group>>(emptyList())
  val groups: StateFlow<List<A1Group>> = _groups

  init {
    viewModelScope.launch {
      A1NotifBus.active.collect { list ->
        val msgs = list.map { sbn ->
          val e = sbn.notification.extras
          val title = (e?.getCharSequence(Notification.EXTRA_TITLE)?.toString()).orEmpty()
          val text  = (e?.getCharSequence(Notification.EXTRA_TEXT )?.toString()).orEmpty()
          A1Msg(sbn.key, sbn.packageName, title, text, sbn.postTime)
        }
        val grouped = msgs.groupBy { it.pkg }.map { (pkg, ms) ->
          val label = try { pm.getApplicationLabel(pm.getApplicationInfo(pkg, 0)).toString() } catch (_: Throwable) { pkg }
          A1Group(pkg, label, ms.sortedByDescending { it.ts })
        }.sortedByDescending { it.messages.firstOrNull()?.ts ?: 0L }
        _groups.value = grouped
      }
    }
  }
}

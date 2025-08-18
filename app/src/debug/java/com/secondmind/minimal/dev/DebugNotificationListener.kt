package com.secondmind.minimal.dev

import android.app.Application
import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

object DebugNotifBus {
  val active = MutableSharedFlow<List<StatusBarNotification>>(replay = 1, extraBufferCapacity = 64)
}

class DebugNotificationListener : NotificationListenerService() {
  private fun publish() {
    val list = try { activeNotifications?.toList().orEmpty() } catch (_: Throwable) { emptyList() }
    DebugNotifBus.active.tryEmit(list)
  }
  override fun onListenerConnected() = publish()
  override fun onNotificationPosted(sbn: StatusBarNotification) = publish()
  override fun onNotificationRemoved(sbn: StatusBarNotification) = publish()
}

data class Msg(val key:String, val pkg:String, val title:String, val text:String, val ts:Long)
data class AppGroup(val pkg:String, val appLabel:String, val messages:List<Msg>)

class PatchInboxViewModel(app: Application) : AndroidViewModel(app) {
  private val _groups = MutableStateFlow<List<AppGroup>>(emptyList())
  val groups: StateFlow<List<AppGroup>> = _groups

  init {
    val pm = app.packageManager
    viewModelScope.launch {
      DebugNotifBus.active.collect { list ->
        val msgs = list.map { sbn ->
          val e = sbn.notification.extras
          val title = (e?.getCharSequence(Notification.EXTRA_TITLE)?.toString()).orEmpty()
          val text  = (e?.getCharSequence(Notification.EXTRA_TEXT )?.toString()).orEmpty()
          Msg(sbn.key, sbn.packageName, title, text, sbn.postTime)
        }
        val grouped = msgs.groupBy { it.pkg }.map { (pkg, ms) ->
          val label = try { pm.getApplicationLabel(pm.getApplicationInfo(pkg, 0)).toString() } catch (_: Throwable) { pkg }
          AppGroup(pkg, label, ms.sortedByDescending { it.ts })
        }.sortedByDescending { it.messages.firstOrNull()?.ts ?: 0L }
        _groups.value = grouped
      }
    }
  }
}

package com.secondmind.minimal.dev

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object PatchNotifBus {
  private val _events = MutableSharedFlow<StatusBarNotification>(extraBufferCapacity = 128)
  val events = _events.asSharedFlow()
  internal fun emit(sbn: StatusBarNotification) { _events.tryEmit(sbn) }
}

class PatchNotificationListener : NotificationListenerService() {
  override fun onNotificationPosted(sbn: StatusBarNotification) {
    PatchNotifBus.emit(sbn)
  }
}

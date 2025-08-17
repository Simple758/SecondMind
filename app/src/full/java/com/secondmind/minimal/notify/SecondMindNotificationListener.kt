package com.secondmind.minimal.notify
import com.secondmind.minimal.InboxStore

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.secondmind.minimal.data.AppDb
import com.secondmind.minimal.data.InboxRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SecondMindNotificationListener : NotificationListenerService() {
  private val scope = CoroutineScope(Dispatchers.IO)
  private val repo by lazy { InboxRepository(AppDb.get(applicationContext).notificationDao()) }

  override fun onNotificationPosted(sbn: StatusBarNotification?) {
    if (sbn == null) return
    scope.launch { repo.onPosted(sbn) }
  }

  override fun onNotificationRemoved(sbn: StatusBarNotification?) {
    if (sbn == null) return
    scope.launch { repo.onRemoved(sbn) }
  }
}

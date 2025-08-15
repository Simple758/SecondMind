package com.secondmind.minimal.notify

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class SecondMindNotificationListener : NotificationListenerService() {
  override fun onNotificationPosted(sbn: StatusBarNotification?) {
    Log.d("SMNotif", "Posted: " + (sbn?.packageName ?: ""))
  }
  override fun onNotificationRemoved(sbn: StatusBarNotification?) {
    Log.d("SMNotif", "Removed: " + (sbn?.packageName ?: ""))
  }
}

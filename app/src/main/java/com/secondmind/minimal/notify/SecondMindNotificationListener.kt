package com.secondmind.minimal.notify

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class SecondMindNotificationListener : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d("SM-Notif", "listener connected")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        Log.d("SM-Notif", "posted from ${sbn.packageName}")
        // TODO: forward to repository if/when we add one
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.d("SM-Notif", "removed from ${sbn.packageName}")
    }
}

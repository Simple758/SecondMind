package com.secondmind.minimal.notify

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.secondmind.minimal.inbox.InboxStore

class SecondMindNotificationListener : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d("SM-Notif", "listener connected")
        try {
            val pm = applicationContext.packageManager
            val list = activeNotifications ?: emptyArray()
            for (sbn in list) {
                val label = try {
                    pm.getApplicationLabel(pm.getApplicationInfo(sbn.packageName, 0)).toString()
                } catch (_: Throwable) { null }
                InboxStore.push(sbn, label)
            }
        } catch (_: Throwable) { /* ignore */ }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        try {
            val pm = applicationContext.packageManager
            val label = try {
                pm.getApplicationLabel(pm.getApplicationInfo(sbn.packageName, 0)).toString()
            } catch (_: Throwable) { null }
            InboxStore.push(sbn, label)
        } catch (_: Throwable) { /* ignore */ }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        try { InboxStore.remove(sbn.key) } catch (_: Throwable) { /* ignore */ }
    }
}

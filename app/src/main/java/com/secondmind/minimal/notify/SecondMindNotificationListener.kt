    package com.secondmind.minimal.notify

import android.content.ComponentName
import android.content.Context
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.secondmind.minimal.inbox.InboxGate
import com.secondmind.minimal.inbox.InboxStore

class SecondMindNotificationListener : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()
        if (!InboxGate.active) return
        Log.d("SM-Notif", "listener connected (gated)")
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
        if (!InboxGate.active) return
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

    companion object {
        // -- GATED -- marker for idempotent patch
        fun triggerRebind(ctx: Context) {
            try {
                requestRebind(ComponentName(ctx, SecondMindNotificationListener::class.java))
            } catch (_: Throwable) { }
        }
    }
}


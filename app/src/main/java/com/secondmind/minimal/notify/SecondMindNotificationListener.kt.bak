package com.secondmind.minimal.notify

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.secondmind.minimal.diag.NotifDiag
import com.secondmind.minimal.inbox.InboxStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SecondMindNotificationListener : NotificationListenerService() {

    override fun onListenerConnected() {
        try { NotifDiag.markConnected(this) } catch (_: Throwable) {}
        InboxStore.connectedAt = System.currentTimeMillis()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        try { NotifDiag.bumpPosted(this) } catch (_: Throwable) {}
        try { GlobalScope.launch(Dispatchers.Default) { InboxStore.push(sbn) } } catch (_: Throwable) {}
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        try { GlobalScope.launch(Dispatchers.Default) { InboxStore.remove(sbn.key) } } catch (_: Throwable) {}
    }
}

package com.secondmind.minimal.notify

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.secondmind.minimal.diag.NotifDiag

class SecondMindNotificationListener : NotificationListenerService() {
  private fun safe(block: () -> Unit) {
    try { block() } catch (t: Throwable) {
      Log.w("SM/NotifListener", "callback failed", t)
    }
  }

  override fun onListenerConnected() = safe {
    NotifDiag.markConnected(applicationContext)
  }

  override fun onNotificationPosted(sbn: StatusBarNotification) = safe {
    // Only bump if there is some visible text/title (avoid noisy bumps)
    val e = sbn.notification.extras
    val hasText = (e?.getCharSequence(Notification.EXTRA_TEXT)?.toString()).orEmpty().isNotBlank() ||
                  (e?.getCharSequence(Notification.EXTRA_TITLE)?.toString()).orEmpty().isNotBlank()
    if (hasText) NotifDiag.bumpPosted(applicationContext)
  }

  override fun onNotificationRemoved(sbn: StatusBarNotification) = safe {
    // no-op for now
  }
}

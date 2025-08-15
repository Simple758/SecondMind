package com.secondmind.minimal.data

import android.service.notification.StatusBarNotification
import kotlinx.coroutines.flow.Flow

class InboxRepository(private val dao: NotificationDao) {
  fun stream(): Flow<List<NotificationEntity>> = dao.streamAll()
  fun byId(id: Long): Flow<NotificationEntity?> = dao.streamById(id)
  suspend fun delete(id: Long) = dao.deleteById(id)
  suspend fun clear() = dao.clearAll()
  suspend fun prune(cutoffMillis: Long) = dao.pruneOlderThan(cutoffMillis)

  suspend fun onPosted(sbn: StatusBarNotification) {
    val n = sbn.notification
    val title = n.extras?.getCharSequence(android.app.Notification.EXTRA_TITLE)?.toString()
    val text = n.extras?.getCharSequence(android.app.Notification.EXTRA_TEXT)?.toString()
    val channel = if (android.os.Build.VERSION.SDK_INT >= 26) n.channelId else null
    val category = n.category
    val e = NotificationEntity(
      key = sbn.key,
      appPackage = sbn.packageName ?: "unknown",
      title = title,
      text = text,
      channel = channel,
      category = category,
      postedAt = sbn.postTime,
      removedAt = null,
      source = "listener",
      extras = null
    )
    dao.upsert(e)
  }

  suspend fun onRemoved(sbn: StatusBarNotification) {
    dao.markRemoved(sbn.key, System.currentTimeMillis())
  }
}

package com.secondmind.minimal.inbox

import android.app.Notification
import android.service.notification.StatusBarNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

data class NotificationItem(
    val key: String,
    val pkg: String,
    val title: String,
    val text: String,
    val ts: Long
)

object InboxStore {
    private val mutex = Mutex()
    private val _items = MutableStateFlow<List<NotificationItem>>(emptyList())
    val items: StateFlow<List<NotificationItem>> get() = _items

    @Volatile var connectedAt: Long = 0L
    @Volatile var seenCount: Long = 0L

    suspend fun push(sbn: StatusBarNotification) {
        mutex.withLock {
            val e = sbn.notification.extras
            val title = (e?.getCharSequence(Notification.EXTRA_TITLE)?.toString()).orEmpty()
            val text  = (e?.getCharSequence(Notification.EXTRA_TEXT )?.toString()).orEmpty()
            val item = NotificationItem(sbn.key, sbn.packageName, title, text, sbn.postTime)

            val list = _items.value.toMutableList()
            val i = list.indexOfFirst { it.key == item.key }
            if (i >= 0) list[i] = item else list += item
            _items.value = list.sortedByDescending { it.ts }
            seenCount++
        }
    }

    suspend fun remove(key: String) {
        mutex.withLock {
            _items.value = _items.value.filterNot { it.key == key }
        }
    }

    fun clear() {
        _items.value = emptyList()
    }
}

package com.secondmind.minimal.inbox

import android.app.Notification
import android.service.notification.StatusBarNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class NotificationItem(
    val key: String,
    val pkg: String,
    val appLabel: String,
    val title: String,
    val text: String,
    val ts: Long
)

object InboxStore {
    private val _items = MutableStateFlow<List<NotificationItem>>(emptyList())
    val items: StateFlow<List<NotificationItem>> = _items

    @Synchronized
    fun push(sbn: StatusBarNotification, appLabel: String?) {
        val e = sbn.notification.extras
        val title = (e?.getCharSequence(Notification.EXTRA_TITLE)?.toString()).orEmpty()
        val text  = (e?.getCharSequence(Notification.EXTRA_TEXT )?.toString()).orEmpty()
        val label = appLabel?.takeIf { it.isNotBlank() } ?: sbn.packageName
        val item = NotificationItem(sbn.key, sbn.packageName, label, title, text, sbn.postTime)
        val list = _items.value.toMutableList().apply {
            removeAll { it.key == item.key }
            add(item)
            sortByDescending { it.ts }
            if (size > 200) subList(200, size).clear()
        }
        _items.value = list
    }

    @Synchronized fun remove(key: String) {
        if (_items.value.isEmpty()) return
        _items.value = _items.value.filterNot { it.key == key }
    }

    @Synchronized fun clear() { _items.value = emptyList() }
}

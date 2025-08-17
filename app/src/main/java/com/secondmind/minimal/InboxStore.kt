package com.secondmind.minimal

import android.app.Notification
import android.content.Context
import android.service.notification.StatusBarNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.atomic.AtomicLong

data class Notif(
  val id: Long,
  val pkg: String,
  val appLabel: String,
  val title: String,
  val text: String,
  val ts: Long
)
data class AppSummary(val appPackage: String, val appLabel: String, val count: Int, val preview: String)
data class AppMessage(val id: Long, val title: String, val text: String, val ts: Long)

object InboxStore {
  private val next = AtomicLong(1)
  private val _items = MutableStateFlow<List<Notif>>(emptyList())
  val items: StateFlow<List<Notif>> = _items

  fun onNotificationPosted(ctx: Context, sbn: StatusBarNotification) {
    val pkg = sbn.packageName ?: return
    val pm = ctx.packageManager
    val label = try { pm.getApplicationLabel(pm.getApplicationInfo(pkg, 0)).toString() } catch (_: Exception) { pkg }
    val extras = sbn.notification.extras
    val title = extras?.getCharSequence(Notification.EXTRA_TITLE)?.toString() ?: ""
    val text = extras?.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: ""
    val n = Notif(next.getAndIncrement(), pkg, label, title, text, sbn.postTime)
    _items.value = (_items.value + n).takeLast(500)
  }

  fun summaries(): List<AppSummary> =
    _items.value.groupBy { it.pkg }.map { (pkg, list) ->
      AppSummary(pkg, list.firstOrNull()?.appLabel ?: pkg, list.size,
        list.firstOrNull { it.text.isNotBlank() }?.text ?: "")
    }.sortedByDescending { it.count }

  fun messagesForPackage(pkg: String): List<AppMessage> =
    _items.value.filter { it.pkg == pkg }.sortedByDescending { it.ts }
      .map { AppMessage(it.id, it.title, it.text, it.ts) }
}

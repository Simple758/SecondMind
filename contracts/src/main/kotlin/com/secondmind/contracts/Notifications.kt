package com.secondmind.contracts
data class NotificationItem(val id: String, val app: String, val text: String, val ts: Long)
interface NotificationsRepo {
    fun apps(): List<String>
    fun allFor(app: String): List<NotificationItem>
}

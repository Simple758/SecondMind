package com.secondmind.stubs
import com.secondmind.contracts.*
class TtsStub : Tts { override fun speak(text: String) { /* no-op */ } }
class NotificationsRepoStub : NotificationsRepo {
    override fun apps() = emptyList<String>()
    override fun allFor(app: String) = emptyList<NotificationItem>()
}

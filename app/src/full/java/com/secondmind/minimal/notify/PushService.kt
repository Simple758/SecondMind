package com.secondmind.minimal.notify

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log

class PushService : FirebaseMessagingService() {
  override fun onNewToken(token: String) {
    Log.d("SMPush", "FCM token: $token")
  }
  override fun onMessageReceived(message: RemoteMessage) {
    Log.d("SMPush", "Push: ${message.data}")
  }
}

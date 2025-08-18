package com.secondmind.minimal.a1

import android.service.notification.StatusBarNotification
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object NotifBus {
  private val _active = MutableSharedFlow<List<StatusBarNotification>>(replay = 1, extraBufferCapacity = 64)
  val active = _active.asSharedFlow()
  fun emit(list: List<StatusBarNotification>) { _active.tryEmit(list) }
}

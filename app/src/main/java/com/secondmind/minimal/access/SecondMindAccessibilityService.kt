package com.secondmind.minimal.access

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class SecondMindAccessibilityService : AccessibilityService() {
  override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
  override fun onInterrupt() {}
}

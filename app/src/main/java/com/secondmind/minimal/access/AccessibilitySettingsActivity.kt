package com.secondmind.minimal.access

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class AccessibilitySettingsActivity : Activity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    startActivity(Intent().setComponent(android.content.ComponentName("com.secondmind.minimal", "com.secondmind.minimal.MainActivity")))
    finish()
  }
}

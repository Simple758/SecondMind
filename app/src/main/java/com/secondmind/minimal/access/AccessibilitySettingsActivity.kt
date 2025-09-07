package com.secondmind.minimal.access

import android.app.Activity
import com.secondmind.minimal.MainActivity
import android.content.Intent
import android.os.Bundle

class AccessibilitySettingsActivity : Activity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    startActivity(Intent(this, MainActivity::class.java))
    finish()
  }
}

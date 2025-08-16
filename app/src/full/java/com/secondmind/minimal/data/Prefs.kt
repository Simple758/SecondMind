package com.secondmind.minimal.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore("prefs")

object Keys {
  val COUNT = intPreferencesKey("count")
  val THEME = stringPreferencesKey("theme")
  val RETENTION_DAYS = intPreferencesKey("retention_days")
  val READER_ENABLED = booleanPreferencesKey("reader_enabled")
  val READER_RATE = floatPreferencesKey("reader_rate")
  val READER_PITCH = floatPreferencesKey("reader_pitch")
}

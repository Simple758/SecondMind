package com.secondmind.minimal.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore("prefs")

object Keys {
  val X_FEED_URL = stringPreferencesKey("x_feed_url")
  val X_NITTER_BASE = stringPreferencesKey("x_nitter_base")
  val X_PROFILES = stringPreferencesKey("x_profiles")
  val X_TTS_COUNT = intPreferencesKey("x_tts_count")
  val READER_VOICE = stringPreferencesKey("reader_voice")
  val COUNT = intPreferencesKey("count")
  val THEME = stringPreferencesKey("theme")
  val RETENTION_DAYS = intPreferencesKey("retention_days")
  val READER_ENABLED = booleanPreferencesKey("reader_enabled")
  val READER_RATE = floatPreferencesKey("reader_rate")
  val READER_PITCH = floatPreferencesKey("reader_pitch")
}

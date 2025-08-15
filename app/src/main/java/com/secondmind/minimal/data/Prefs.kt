package com.secondmind.minimal.data

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore("prefs")

object Keys {
  val COUNT = intPreferencesKey("count")
  val THEME = stringPreferencesKey("theme")
  val RETENTION_DAYS = intPreferencesKey("retention_days")
}

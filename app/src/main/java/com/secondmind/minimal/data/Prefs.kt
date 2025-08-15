package com.secondmind.minimal.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore("prefs")
object Keys {
  val COUNT = androidx.datastore.preferences.core.intPreferencesKey("count")
  val THEME = androidx.datastore.preferences.core.stringPreferencesKey("theme") // system/light/dark
}

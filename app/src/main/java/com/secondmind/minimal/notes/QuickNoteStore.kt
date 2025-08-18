package com.secondmind.minimal.notes

import android.content.Context
import androidx.core.content.edit

private const val PREFS = "quick_note_prefs"
private const val KEY = "quick_note_text"

object QuickNoteStore {
    fun load(ctx: Context): String =
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY, "") ?: ""
    fun save(ctx: Context, value: String) {
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit { putString(KEY, value) }
    }
}

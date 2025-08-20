package com.secondmind.minimal.feature.storage

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject

object JsonPrefs {
    private const val FILE = "secondmind_json_prefs"
    private fun prefs(ctx: Context): SharedPreferences =
        ctx.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    fun put(ctx: Context, key: String, obj: JSONObject) {
        prefs(ctx).edit().putString(key, obj.toString()).apply()
    }
    fun getObject(ctx: Context, key: String): JSONObject? =
        prefs(ctx).getString(key, null)?.let { runCatching { JSONObject(it) }.getOrNull() }

    fun put(ctx: Context, key: String, arr: JSONArray) {
        prefs(ctx).edit().putString(key, arr.toString()).apply()
    }
    fun getArray(ctx: Context, key: String): JSONArray? =
        prefs(ctx).getString(key, null)?.let { runCatching { JSONArray(it) }.getOrNull() }

    fun putString(ctx: Context, key: String, value: String) {
        prefs(ctx).edit().putString(key, value).apply()
    }
    fun getString(ctx: Context, key: String, def: String? = null): String? =
        prefs(ctx).getString(key, def)

    fun remove(ctx: Context, key: String) { prefs(ctx).edit().remove(key).apply() }
}

package com.secondmind.minimal.memory
import android.content.Context
import androidx.core.content.edit
import org.json.JSONArray
import org.json.JSONObject

private const val PREFS="memory_prefs"
private const val KEY_PANELS="panel_counts"
private const val KEY_NEWS="news_titles"

object MemoryStore {
  private fun P(ctx: Context) = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

  fun recordPanelOpen(ctx: Context, p: String) {
    val pfs = P(ctx)
    val o = JSONObject(pfs.getString(KEY_PANELS, "{}") ?: "{}")
    o.put(p, o.optInt(p, 0) + 1)
    pfs.edit { putString(KEY_PANELS, o.toString()) }
  }

  fun recordNewsOpen(ctx: Context, title: String) {
    val pfs = P(ctx)
    val a = JSONArray(pfs.getString(KEY_NEWS, "[]") ?: "[]")
    if (a.length() == 0 || a.getString(a.length() - 1) != title) a.put(title)
    val t = JSONArray()
    val st = (a.length() - 50).coerceAtLeast(0)
    for (i in st until a.length()) t.put(a.getString(i))
    pfs.edit { putString(KEY_NEWS, t.toString()) }
  }

  fun recentNewsTitles(ctx: Context, max: Int = 10): List<String> {
    val a = JSONArray(P(ctx).getString(KEY_NEWS, "[]") ?: "[]")
    val out = ArrayList<String>()
    for (i in (a.length() - 1) downTo 0) {
      out.add(a.getString(i))
      if (out.size >= max) break
    }
    return out
  }

  fun resumeText(ctx: Context): String? {
    val n = recentNewsTitles(ctx, 3)
    return if (n.isNotEmpty())
      "You viewed ${n.size} news item" + (if (n.size > 1) "s" else "") + " recently. Want to catch up?"
    else null
  }
}

package com.secondmind.minimal.knowledge
import android.content.Context
import androidx.core.content.edit
import org.json.JSONArray
import org.json.JSONObject
private const val PREFS = "knowledge_prefs"
private const val KEY = "items"
data class KnowledgeItem(val title: String, val ts: Long)
object KnowledgeRepo {
  private fun P(ctx: Context) = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
  fun addText(ctx: Context, text: String) {
    val a = JSONArray(P(ctx).getString(KEY, "[]") ?: "[]")
    a.put(JSONObject().put("type", "text").put("title", text).put("ts", System.currentTimeMillis()))
    val t = JSONArray()
    val st = (a.length() - 50).coerceAtLeast(0)
    for (i in st until a.length()) t.put(a.getJSONObject(i))
    P(ctx).edit { putString(KEY, t.toString()) }
  }
  fun last(ctx: Context, max: Int = 3): List<KnowledgeItem> {
    val a = JSONArray(P(ctx).getString(KEY, "[]") ?: "[]")
    val out = ArrayList<KnowledgeItem>()
    for (i in (a.length() - 1) downTo 0) {
      val o = a.getJSONObject(i)
      out.add(KnowledgeItem(o.optString("title"), o.optLong("ts")))
      if (out.size >= max) break
    }
    return out
  }
}

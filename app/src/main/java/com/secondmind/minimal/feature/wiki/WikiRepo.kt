package com.secondmind.minimal.feature.wiki

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object WikiRepo {
    data class Summary(val title: String, val extract: String, val url: String, val thumb: String?)

    fun randomSummary(timeoutMs: Int = 5000): Summary? = runCatching {
        val conn = (URL("https://en.wikipedia.org/api/rest_v1/page/random/summary")
            .openConnection() as HttpURLConnection).apply {
            connectTimeout = timeoutMs; readTimeout = timeoutMs; requestMethod = "GET"
            setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 13) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0 Mobile Safari/537.36 SecondMind/1.0")
        }
        conn.inputStream.use {
            val txt = it.readBytes().toString(Charsets.UTF_8)
            val j = JSONObject(txt)
            val title = j.optString("title")
            val extract = j.optString("extract")
            val url = j.optJSONObject("content_urls")?.optJSONObject("desktop")?.optString("page")
                ?: j.optString("content_urls")
            val thumb = j.optJSONObject("thumbnail")?.optString("source")
            if (title.isNotBlank() && extract.isNotBlank() && url.isNotBlank())
                Summary(title, extract, url, thumb) else null
        }
    }.getOrNull()
}
// ====== Added by ui_wiki_patch (safe append) ======
/**
 * Minimal search and summary helpers using Wikipedia REST.
 * No external deps; uses java.net + org.json (Android built-in).
 */
private fun httpGetJson(url: String): org.json.JSONObject {
    val u = java.net.URL(url)
    val c = (u.openConnection() as java.net.HttpURLConnection).apply {
        requestMethod = "GET"
        setRequestProperty("Accept", "application/json")
        setRequestProperty("User-Agent", "SecondMind/1.0 (Android)")
        connectTimeout = 8000
        readTimeout = 8000
    }
    return try {
        val text = c.inputStream.bufferedReader().use { it.readText() }
        org.json.JSONObject(text)
    } finally {
        c.disconnect()
    }
}


fun searchTitles(query: String, limit: Int = 5): List<String> {
    return try {
        val enc = java.net.URLEncoder.encode(query, "UTF-8")
        val url = "https://en.wikipedia.org/w/rest.php/v1/search/title?q=$enc&limit=$limit"
        android.util.Log.d("WikiRepo", "searchTitles: URL=$url")
        val j = httpGetJson(url)
        android.util.Log.d("WikiRepo", "searchTitles: Response=${j.toString().take(300)}")
        val arr = j.optJSONArray("pages")
        if (arr == null) {
            android.util.Log.e("WikiRepo", "searchTitles: No 'pages' array. Full response: ${j.toString()}")
            return emptyList()
        }
        buildList {
            for (i in 0 until arr.length()) {
                val obj = arr.optJSONObject(i) ?: continue
                val t = obj.optString("title")
                if (t.isNotBlank()) add(t)
            }
        }.also { android.util.Log.d("WikiRepo", "searchTitles: Found ${it.size} titles: $it") }
    } catch (e: Throwable) {
        android.util.Log.e("WikiRepo", "searchTitles: Exception: ${e.javaClass.simpleName}: ${e.message}", e)
        emptyList()
    }
}














data class WikiSummary(val title: String, val extract: String, val thumbnail: String? = null)

fun getSummary(title: String): WikiSummary? {
    return try {
        val enc = java.net.URLEncoder.encode(title, "UTF-8")
        val j = httpGetJson("https://en.wikipedia.org/api/rest_v1/page/summary/$enc")
        val extract = j.optString("extract")
        val thumb = j.optJSONObject("thumbnail")?.optString("source")
        if (extract.isNullOrBlank()) null else WikiSummary(title = j.optString("title", title), extract = extract, thumbnail = thumb)
    } catch (_: Throwable) { null }
}

// Simple 7-day TTL cache using SharedPreferences (JSON map)
fun cacheGet(context: android.content.Context, key: String): String? {
    val sp = context.getSharedPreferences("wiki_cache", 0)
    val now = System.currentTimeMillis()
    return try {
        val raw = sp.getString(key, null) ?: return null
        val j = org.json.JSONObject(raw)
        val ttl = j.optLong("ttl", 0L)
        val until = j.optLong("until", 0L)
        if (ttl <= 0L || until < now) { null } else j.optString("value", null)
    } catch (_: Throwable) { null }
}
fun cachePut(context: android.content.Context, key: String, value: String, days: Int = 7) {
    val sp = context.getSharedPreferences("wiki_cache", 0)
    val now = System.currentTimeMillis()
    val until = now + days * 24L * 3600_000L
    val j = org.json.JSONObject().put("value", value).put("ttl", days).put("until", until)
    sp.edit().putString(key, j.toString()).apply()
}
// ====== end ui_wiki_patch append ======

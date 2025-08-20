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
            setRequestProperty("User-Agent", "SecondMind/1.0")
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

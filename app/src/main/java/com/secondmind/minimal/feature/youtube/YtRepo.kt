package com.secondmind.minimal.feature.youtube

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object YtRepo {
    data class OEmbed(val title: String, val author: String, val thumbnail: String)
    suspend fun fetchOEmbed(videoUrl: String, timeoutMs: Int = 6000): OEmbed? =
        withContext(Dispatchers.IO) {
            runCatching {
                val endpoint = "https://www.youtube.com/oembed?format=json&url=$videoUrl"
                val conn = (URL(endpoint).openConnection() as HttpURLConnection).apply {
                    connectTimeout = timeoutMs
                    readTimeout = timeoutMs
                    requestMethod = "GET"
                    setRequestProperty("User-Agent", "SecondMind/1.0 (+oembed)")
                }
                conn.inputStream.use {
                    val txt = it.readBytes().toString(Charsets.UTF_8)
                    val j = JSONObject(txt)
                    val title = j.optString("title")
                    val author = j.optString("author_name")
                    val thumb = j.optString("thumbnail_url")
                    if (title.isNotBlank() && thumb.isNotBlank()) OEmbed(title, author, thumb) else null
                }
            }.getOrNull()
        }
}

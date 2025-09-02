package com.secondmind.minimal.feature.news

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.net.URLEncoder

object NewsApi {
    // Embedded key (dev only, user approved to embed)
    private const val API_KEY: String = "45b180a1ea0f45018aa89b36c1e49cf1"
    private val client = OkHttpClient()

    private fun get(url: String): String {
        val req = Request.Builder()
            .url(url)
            .header("X-Api-Key", API_KEY)
            .build()
        client.newCall(req).execute().use { resp ->
            if (!resp.isSuccessful) error("HTTP ${'$'}{resp.code}")
            return resp.body?.string() ?: error("empty body")
        }
    }

    fun fetch(tab: NewsTab): List<Article> {
        val base = "https://newsapi.org/v2/"
        val url = when (tab) {
            NewsTab.ForYou -> "${base}top-headlines?language=en&pageSize=25"
            NewsTab.Tech -> "${base}top-headlines?category=technology&language=en&pageSize=25"
            NewsTab.Markets -> "${base}everything?q=" +
                URLEncoder.encode("(stocks OR market OR finance OR economy)", "UTF-8") +
                "&language=en&sortBy=publishedAt&pageSize=25"
            NewsTab.World -> "${base}top-headlines?category=general&language=en&pageSize=25"
            NewsTab.Sports -> "${base}top-headlines?category=sports&language=en&pageSize=25"
            NewsTab.Crypto -> "${base}everything?q=" +
                URLEncoder.encode("(crypto OR bitcoin OR ethereum)", "UTF-8") +
                "&language=en&sortBy=publishedAt&pageSize=25"
        }
        val raw = get(url)
        val json = JSONObject(raw)
        val arr = json.optJSONArray("articles") ?: return emptyList()
        val out = ArrayList<Article>(arr.length())
        for (i in 0 until arr.length()) {
            val a = arr.getJSONObject(i)
            val s = a.optJSONObject("source")
            out.add(
                Article(
                    title = a.optString("title", null),
                    url = a.optString("url", null),
                    urlToImage = a.optString("urlToImage", null),
                    source = NewsSource(id = s?.optString("id"), name = s?.optString("name")),
                    publishedAt = a.optString("publishedAt", null),
                )
            )
        }
        return out
    }
}

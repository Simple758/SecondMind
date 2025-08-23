package com.secondmind.minimal.feature.news

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URI
import java.net.URLEncoder
import kotlin.math.min

data class NewsItem(val title: String, val link: String, val source: String)

object NewsRepo {
  private val defaultFeeds: Map<String, List<String>> = mapOf(
    "Tech" to listOf(
      "https://www.theverge.com/rss/index.xml",
      "https://feeds.arstechnica.com/arstechnica/index"
    ),
    "Markets" to listOf(
      "https://feeds.reuters.com/reuters/businessNews",
      "https://www.ft.com/rss/home" // may partially work; fallback covers it
    ),
    "World" to listOf(
      "http://feeds.bbci.co.uk/news/world/rss.xml",
      "https://feeds.reuters.com/Reuters/worldNews"
    ),
    "Sports" to listOf(
      "http://feeds.bbci.co.uk/sport/rss.xml?edition=uk",
      "https://www.espn.com/espn/rss/news"
    ),
    "Science/AI" to listOf(
      "https://www.nature.com/subjects/artificial-intelligence/rss",
      "https://news.ycombinator.com/rss"
    )
  )

  private fun googleNewsFallback(sector: String): String {
    val q = URLEncoder.encode(sector, "UTF-8")
    return "https://news.google.com/rss/search?q=$q&hl=en-US&gl=US&ceid=US:en"
  }

  private fun hostOf(url: String): String =
    try { URI(url).host?.removePrefix("www.") ?: "news" } catch (_: Throwable) { "news" }

  private fun parseRss(doc: Document, limit: Int): List<NewsItem> =
    doc.select("item").take(limit).mapNotNull { it ->
      val title = it.selectFirst("title")?.text()?.trim().orEmpty()
      val link = it.selectFirst("link")?.text()?.trim().orEmpty()
      if (title.isBlank() || link.isBlank()) null
      else NewsItem(title, link, source = hostOf(link))
    }

  private fun parseAtom(doc: Document, limit: Int): List<NewsItem> =
    doc.select("entry").take(limit).mapNotNull { e ->
      val title = e.selectFirst("title")?.text()?.trim().orEmpty()
      val link = e.selectFirst("link[href]")?.attr("href")?.trim().orEmpty()
      if (title.isBlank() || link.isBlank()) null
      else NewsItem(title, link, source = hostOf(link))
    }

  private fun parse(doc: Document, limit: Int): List<NewsItem> {
    val rss = parseRss(doc, limit)
    if (rss.isNotEmpty()) return rss
    val atom = parseAtom(doc, limit)
    return atom
  }

  suspend fun fetch(sector: String, limit: Int = 5, perFeedTimeoutMs: Int = 8000): List<NewsItem> =
    withContext(Dispatchers.IO) {
      val feeds = (defaultFeeds[sector] ?: emptyList()) + googleNewsFallback(sector)
      var lastErr: Throwable? = null
      for (f in feeds) {
        try {
          val doc = Jsoup.connect(f)
            .userAgent("SecondMind/1.0 (+android)")
            .timeout(perFeedTimeoutMs)
            .get()
          val items = parse(doc, min(limit, 10))
          if (items.isNotEmpty()) return@withContext items
        } catch (t: Throwable) { lastErr = t }
      }
      if (lastErr != null) throw lastErr
      emptyList()
    }
}

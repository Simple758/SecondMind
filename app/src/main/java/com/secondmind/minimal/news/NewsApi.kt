package com.secondmind.minimal.news

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URL

data class NewsItem(
  val title: String,
  val url: String,
  val imageUrl: String? = null,
  val source: String? = null
)

object NewsApi {
  /**
   * Very small, network-simple fetcher using Jsoup.
   * - Pulls a few sources (you can extend).
   * - Extracts og:image / twitter:image or first image as thumbnail.
   * NOTE: This runs at runtime; nothing here requires kapt/retrofit.
   */
  suspend fun fetchTopHeadlines(): List<NewsItem> {
    val sources = listOf(
      "https://www.theverge.com/",
      "https://techcrunch.com/",
      "https://news.ycombinator.com/"
    )
    val items = mutableListOf<NewsItem>()
    for (src in sources) {
      try {
        val doc = Jsoup.connect(src).timeout(8000).get()
        items += extractFrom(doc, src)
      } catch (_: Throwable) {
        // swallow per-source failures
      }
    }
    // de-dup by URL, keep first occurrence
    return items.distinctBy { it.url }
  }

  private fun extractFrom(doc: Document, src: String): List<NewsItem> {
    val baseHost = try { URL(src).host } catch (_: Throwable) { null }
    val results = mutableListOf<NewsItem>()

    // Simple strategy: pick prominent links with titles
    val anchors = doc.select("a[href]").take(120)
    for (a in anchors) {
      val title = (a.attr("title").ifBlank { a.text() }).trim()
      val href = a.absUrl("href")
      if (title.length < 24) continue
      if (!href.startsWith("http")) continue

      val thumb = ogImage(doc) ?: twitterImage(doc)
      results += NewsItem(
        title = title,
        url = href,
        imageUrl = thumb,
        source = baseHost
      )
      if (results.size >= 20) break
    }
    return results
  }

  private fun ogImage(doc: Document): String? =
    doc.selectFirst("meta[property=og:image], meta[name=og:image]")?.attr("content")?.takeIf { it.isNotBlank() }

  private fun twitterImage(doc: Document): String? =
    doc.selectFirst("meta[name=twitter:image], meta[property=twitter:image]")?.attr("content")?.takeIf { it.isNotBlank() }
}

package com.secondmind.minimal.news

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser
import java.net.URL

data class NewsItem(
  val title: String,
  val url: String,
  val imageUrl: String? = null,
  val source: String? = null
)

/**
 * Reliable RSS-based fetcher:
 *  - TechCrunch: https://techcrunch.com/feed/
 *  - Hacker News: https://hnrss.org/frontpage
 *  - The Verge: https://www.theverge.com/rss/index.xml
 *
 * We parse <item><title> and <link>, and try common image carriers:
 *   - <media:content url="..."> or <media:thumbnail url="...">
 *   - <enclosure type="image/..."> url
 * If no image is present, imageUrl stays null (UI handles it).
 */
object NewsApi {
  suspend fun fetchTopHeadlines(): List<NewsItem> {
    val sources = listOf(
      "https://techcrunch.com/feed/",
      "https://hnrss.org/frontpage",
      "https://www.theverge.com/rss/index.xml"
    )
    val all = mutableListOf<NewsItem>()
    for (feed in sources) {
      try {
        all += fetchRss(feed)
      } catch (_: Throwable) { /* ignore per-feed errors */ }
    }
    return all
      .distinctBy { it.url }
      .take(50)
  }

  private fun fetchRss(url: String): List<NewsItem> {
    val doc = Jsoup.connect(url)
      .timeout(10000)
      .userAgent("SecondMind/1.0 (+android)")
      .get()

    // Re-parse as XML to simplify tag extraction
    val xml = Jsoup.parse(doc.outerHtml(), "", Parser.xmlParser())
    val host = try { URL(url).host } catch (_: Throwable) { null }

    val out = mutableListOf<NewsItem>()
    val items = xml.select("item")
    for (item in items) {
      val title = item.selectFirst("title")?.text()?.trim().orEmpty()
      val link  = item.selectFirst("link")?.text()?.trim().orEmpty()
      if (title.length < 10) continue
      if (!link.startsWith("http")) continue

      val media = item.selectFirst("media|content, media|thumbnail")
      val enclosure = item.selectFirst("enclosure[url]")
      val image = when {
        media?.hasAttr("url") == true -> media.attr("url")
        enclosure?.attr("type")?.startsWith("image/") == true -> enclosure.attr("url")
        else -> null
      }?.takeIf { it.startsWith("http") }

      out += NewsItem(
        title = title,
        url = link,
        imageUrl = image,
        source = host
      )
      if (out.size >= 20) break
    }
    return out
  }
}

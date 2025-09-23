package com.secondmind.minimal.news

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.w3c.dom.Element
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Locale
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.math.min

/**
 * Direct, keyless RSS headlines from official feeds:
 * - Reuters Top News
 * - BBC News
 * - ESPN General
 * - CoinDesk
 *
 * Image extraction order:
 *   media:thumbnail@url -> media:content[type=image]@url -> enclosure[type=image]@url -> first <img src> in description/content:encoded
 * Dedupe: canonicalized link (drop utm/gclid/fbclid), cap [limit] (default 80).
 *
 * Usage (drop-in when ready):
 *   val items: List<NewsItem> = RssNewsApi.fetchTopHeadlines()
 *   // NewsItem is already defined in this package (NewsApi.kt).
 */
object RssNewsApi {

  private data class SourceFeed(val name: String, val url: String)

  private val DEFAULT_FEEDS = listOf(
    SourceFeed("Reuters",  "https://feeds.reuters.com/reuters/topNews"),
    SourceFeed("BBC",      "https://feeds.bbci.co.uk/news/rss.xml"),
    SourceFeed("ESPN",     "https://www.espn.com/espn/rss/news"),
    SourceFeed("CoinDesk", "https://www.coindesk.com/arc/outboundfeeds/rss/"),
    // --- Added market-focused sources (via Google News RSS) ---
    SourceFeed("CNBC (Markets)", "https://news.google.com/rss/search?q=site:cnbc.com+markets"),
    SourceFeed("Benzinga",       "https://news.google.com/rss/search?q=site:benzinga.com+markets"),
    SourceFeed("Yahoo Finance",  "https://news.google.com/rss/search?q=site:finance.yahoo.com+news"),
    SourceFeed("MarketWatch",    "https://news.google.com/rss/search?q=site:marketwatch.com+markets")
  )

  suspend fun fetchTopHeadlines(limit: Int = 80): List<NewsItem> = withContext(Dispatchers.IO) {
    val all = mutableListOf<NewsItem>()
    for (sf in DEFAULT_FEEDS) {
      runCatching { fetchFeed(sf) }
        .onSuccess { all += it }
        .onFailure { /* ignore single-feed errors */ }
    }
    val seen = LinkedHashMap<String, NewsItem>()
    for (n in all) {
      val key = canonicalize(n.url)
      if (!seen.containsKey(key)) seen[key] = n
      if (seen.size >= limit) break
    }
    seen.values.toList()
  }

  private fun fetchFeed(sf: SourceFeed): List<NewsItem> {
    val conn = (URL(sf.url).openConnection() as HttpURLConnection).apply {
      connectTimeout = 10000
      readTimeout = 10000
      instanceFollowRedirects = true
      setRequestProperty("User-Agent", "SecondMind/1.0 (Android)")
      setRequestProperty("Accept", "application/rss+xml, application/xml, text/xml, */*;q=0.8")
    }
    conn.inputStream.use { raw ->
      val ins = BufferedInputStream(raw)
      return parseRss(ins, sf.name)
    }
  }

  // ---------------- XML parsing ----------------
  private fun parseRss(stream: InputStream, sourceName: String): List<NewsItem> {
    val factory = DocumentBuilderFactory.newInstance().apply { isNamespaceAware = true }
    val doc = factory.newDocumentBuilder().parse(stream)
    doc.documentElement.normalize()

    val items = doc.getElementsByTagName("item")
    val entries = if (items.length == 0) doc.getElementsByTagName("entry") else items

    val out = ArrayList<NewsItem>(min(60, entries.length))
    for (i in 0 until entries.length) {
      val node = entries.item(i)
      val el = node as? Element ?: continue

      val title = firstText(el, "title")?.trim().orEmpty()
      if (title.isEmpty()) continue

      val link = resolveLink(el) ?: continue
      val image = resolveImage(el)
      val host = runCatching { URL(link).host }.getOrNull()

      out += NewsItem(
        title = title,
        url = link,
        imageUrl = image,
        source = host
      )
    }
    return out
  }

  // --------------- helpers: link/image/date ---------------
  private fun resolveLink(item: Element): String? {
    // Safe replacement (no non-local return inside let)
    val linkText = firstText(item, "link")?.trim()
    if (linkText != null && linkText.startsWith("http")) return linkText

    // Atom <link rel="alternate" href="..."/>
    getFirstByTag(item, "link")?.let { l ->
      val href = l.getAttribute("href")
      if (!href.isNullOrBlank() && href.startsWith("http")) return href
    }

    // GUID fallback if permalink
    getFirstByTag(item, "guid")?.let { g ->
      val isPerma = g.getAttribute("isPermaLink")
      val txt = g.textContent?.trim()
      if (isPerma.equals("true", ignoreCase = true) && txt?.startsWith("http") == true) return txt
    }
    return null
  }

  private fun resolveImage(item: Element): String? {
    // media:thumbnail url="..."
    val thumb = attrOnFirstNS(item, "*", "thumbnail", "url")
    if (thumb != null && thumb.startsWith("http")) return thumb

    // media:content type starts with image
    val content = getByTagNS(item, "*", "content")
    for (e in content) {
      val type = e.getAttribute("type") ?: ""
      val url  = e.getAttribute("url") ?: ""
      if (type.lowercase(Locale.ROOT).startsWith("image") && url.startsWith("http")) return url
    }

    // enclosure type=image
    val encs = getByTag(item, "enclosure")
    for (e in encs) {
      val type = e.getAttribute("type") ?: ""
      val url  = e.getAttribute("url") ?: ""
      if (type.lowercase(Locale.ROOT).startsWith("image") && url.startsWith("http")) return url
    }

    // description <img src="...">
    val desc = firstText(item, "description")
    if (desc != null) {
      val fromDesc = extractFirstImg(desc)
      if (fromDesc != null) return fromDesc
    }

    // content:encoded (namespaced)
    val enc = firstTextNSLocal(item, "encoded")
    if (enc != null) {
      val fromEnc = extractFirstImg(enc)
      if (fromEnc != null) return fromEnc
    }

    return null
  }

  private fun extractFirstImg(html: String): String? {
    val rx = Regex("""<img[^>]+src=['"]([^'" >]+)['"]""", RegexOption.IGNORE_CASE)
    return rx.find(html)?.groupValues?.getOrNull(1)?.takeIf { it.startsWith("http") }
  }

  @Suppress("unused")
  private fun resolvePublishedMillis(item: Element): Long? {
    val cands = listOf("pubDate", "published", "updated")
    val raw = cands.firstNotNullOfOrNull { firstText(item, it)?.trim()?.takeIf { t -> t.isNotEmpty() } } ?: return null
    val fmts = listOf(
      "EEE, dd MMM yyyy HH:mm:ss Z",
      "EEE, dd MMM yyyy HH:mm Z",
      "yyyy-MM-dd'T'HH:mm:ssXXX",
      "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
    )
    for (p in fmts) {
      try {
        val sdf = SimpleDateFormat(p, Locale.US)
        return sdf.parse(raw)?.time
      } catch (_: Throwable) { }
    }
    return null
  }

  // --------------- XML utils ---------------
  private fun firstText(parent: Element, tag: String): String? =
    getFirstByTag(parent, tag)?.textContent

  private fun firstTextNSLocal(parent: Element, localName: String): String? =
    parent.getElementsByTagNameNS("*", localName).item(0)?.textContent

  private fun getFirstByTag(parent: Element, tag: String): Element? =
    parent.getElementsByTagName(tag).item(0) as? Element

  private fun getByTag(parent: Element, tag: String): List<Element> {
    val nl = parent.getElementsByTagName(tag)
    return (0 until nl.length).mapNotNull { nl.item(it) as? Element }
  }

  private fun getByTagNS(parent: Element, ns: String, local: String): List<Element> {
    val nl = parent.getElementsByTagNameNS(ns, local)
    return (0 until nl.length).mapNotNull { nl.item(it) as? Element }
  }

  // --------------- link canonicalization ---------------
  private fun canonicalize(u: String): String {
    return try {
      val url = URL(u)
      val base = url.protocol + "://" + url.host + (if (url.port != -1) ":" + url.port else "") + url.path
      val q = url.query?.split("&")
        ?.filterNot { it.startsWith("utm_") || it.startsWith("gclid") || it.startsWith("fbclid") }
        ?.sorted()
        ?.joinToString("&")
      base + (if (!q.isNullOrBlank()) "?$q" else "")
    } catch (_: Throwable) {
      u
    }
  }

  // --------------- helper now included ---------------
  private fun attrOnFirstNS(parent: Element, ns: String, local: String, attr: String): String? {
    val nl = parent.getElementsByTagNameNS(ns, local)
    for (i in 0 until nl.length) {
      val e = nl.item(i) as? Element ?: continue
      val v = e.getAttribute(attr) ?: ""
      if (v.isNotBlank()) return v
    }
    return null
  }
}

package com.secondmind.minimal.feature.x

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URLEncoder

data class XPost(val author: String?, val text: String, val link: String?)

object XRepo {
  private val MIRRORS = listOf(
    "https://nitter.net",
    "https://nitter.poast.org",
    "https://nitter.privacydev.net",
    "https://nitter.futo.org"
  )

  private fun normBase(b: String?): String? {
    val t = b?.trim()?.removeSuffix("/") ?: return null
    return if (t.startsWith("http")) t else "https://$t"
  }
  private fun toNitter(base: String, url: String): String {
    val b = base.removeSuffix("/")
    return url.trim()
      .replaceFirst(Regex("^https?://(twitter|x)\\.com"), b)
      .replaceFirst(Regex("^https?://mobile\\.twitter\\.com"), b)
      .replaceFirst(Regex("^https?://nitter\\.[^/]+"), b)
  }
  private fun toXLink(nitterHref: String?): String? {
    if (nitterHref.isNullOrBlank()) return null
    return nitterHref.replaceFirst(Regex("^https?://nitter\\.[^/]+"), "https://x.com")
  }
  private fun parseTimeline(doc: Document, limit: Int): List<XPost> {
    var items = doc.select("div.timeline > div.timeline-item")
    if (items.isEmpty()) items = doc.select("div.timeline-item")
    if (items.isEmpty()) items = doc.select("ol#timeline > li")
    return items.take(limit).mapNotNull {
      val who = it.selectFirst(".fullname, .username, a.fullname")?.text()
      val text = (it.selectFirst(".tweet-content")?.text() ?: it.selectFirst("p")?.text() ?: "")
        .replace("\\s+".toRegex(), " ").trim()
      if (text.isBlank()) null
      else XPost(who, text, toXLink(it.selectFirst("a.tweet-link")?.absUrl("href")))
    }
  }

  /** Fetch from a public List URL (user base or mirrors) */
  suspend fun fetchList(nitterBase: String?, listUrl: String, count: Int): List<XPost> = withContext(Dispatchers.IO) {
    val bases = LinkedHashSet<String>()
    normBase(nitterBase)?.let { bases.add(it) }
    MIRRORS.forEach { bases.add(it) }
    var lastErr: Throwable? = null
    for (b in bases) {
      try {
        val u = toNitter(b, listUrl)
        val doc = Jsoup.connect(u).userAgent("SecondMind/1.0 (+android)").timeout(20000).get()
        val posts = parseTimeline(doc, count)
        if (posts.isNotEmpty()) return@withContext posts
      } catch (t: Throwable) { lastErr = t }
    }
    if (lastErr != null) throw lastErr
    emptyList()
  }

  /** Fetch from multiple public profiles (comma-separated handles, without @). */
  suspend fun fetchProfiles(nitterBase: String?, handles: List<String>, perUser: Int, totalLimit: Int): List<XPost> =
    withContext(Dispatchers.IO) {
      val bases = LinkedHashSet<String>()
      normBase(nitterBase)?.let { bases.add(it) }
      MIRRORS.forEach { bases.add(it) }
      val out = ArrayList<XPost>()
      for (h in handles.filter { it.isNotBlank() }) {
        val path = "/${h.trim().removePrefix("@")}"
        for (b in bases) {
          try {
            val doc = Jsoup.connect(b + path).userAgent("SecondMind/1.0 (+android)").timeout(15000).get()
            out += parseTimeline(doc, perUser)
            break
          } catch (_: Throwable) { /* try next base */ }
        }
        if (out.size >= totalLimit) break
      }
      return@withContext out.take(totalLimit)
    }

  /** Fallback if nothing configured: simple search on Nitter */
  suspend fun searchFallback(nitterBase: String?, query: String, count: Int): List<XPost> =
    withContext(Dispatchers.IO) {
      val bases = LinkedHashSet<String>()
      normBase(nitterBase)?.let { bases.add(it) }
      MIRRORS.forEach { bases.add(it) }
      for (b in bases) {
        try {
          val doc = Jsoup.connect("$b/search?f=tweets&q=" + URLEncoder.encode(query, "UTF-8"))
            .userAgent("SecondMind/1.0 (+android)").timeout(20000).get()
          val posts = parseTimeline(doc, count)
          if (posts.isNotEmpty()) return@withContext posts
        } catch (_: Throwable) {}
      }
      emptyList()
    }
}

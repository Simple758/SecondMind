package com.secondmind.minimal.feature.tg

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

data class TgPost(val channel: String, val text: String, val link: String?)

object TelegramRepo {
  private const val UA =
    "Mozilla/5.0 (Linux; Android 13; Pixel) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0 Mobile Safari/537.36"

  private suspend fun fetchDoc(url: String): Document? = withContext(Dispatchers.IO) {
    try { Jsoup.connect(url).userAgent(UA).timeout(20000).get() } catch (_: Throwable) { null }
  }

  private suspend fun fetchAuto(handle: String, preferProxy: Boolean): Document? {
    val direct = "https://t.me/s/$handle"
    val prox1  = "https://r.jina.ai/https://t.me/s/$handle"
    val prox2  = "https://r.jina.ai/http://t.me/s/$handle"
    val order = if (preferProxy) listOf(prox1, prox2, direct) else listOf(direct, prox1, prox2)
    for (u in order) { fetchDoc(u)?.let { return it } }
    return null
  }

  private suspend fun fetchForceProxy(handle: String): Document? {
    val prox1 = "https://r.jina.ai/https://t.me/s/$handle"
    val prox2 = "https://r.jina.ai/http://t.me/s/$handle"
    return fetchDoc(prox1) ?: fetchDoc(prox2)
  }

  private suspend fun fetchDirectOnly(handle: String): Document? =
    fetchDoc("https://t.me/s/$handle")

  private fun Elements.safeSelect(css: String) = try { this.select(css) } catch (_: Throwable) { Elements() }

  private fun parse(doc: Document, handle: String, max: Int): List<TgPost> {
    var items = doc.select(".tgme_widget_message_wrap")
    if (items.isEmpty()) items = doc.select("article")  // fallback heuristics

    val out = ArrayList<TgPost>(max)
    for (el in items) {
      val text = (el.selectFirst(".tgme_widget_message_text")?.text()
        ?: el.selectFirst(".js-message_text")?.text()
        ?: el.text())
        .replace("\\s+".toRegex(), " ").trim()

      if (text.isBlank()) continue

      val a = el.selectFirst("a.tgme_widget_message_date") ?: el.selectFirst("a[href*=\"/$handle/\"]")
      val link = a?.absUrl("href") ?: "https://t.me/$handle"

      out += TgPost(channel = handle, text = text, link = link)
      if (out.size >= max) break
    }
    return out
  }

  /**
   * mode: "auto" (default), "force", "direct"
   */
  suspend fun fetchChannel(handle: String, per: Int, mode: String, preferProxy: Boolean): List<TgPost> {
    val h = handle.trim().lowercase()
    val doc = when (mode) {
      "force"  -> fetchForceProxy(h)
      "direct" -> fetchDirectOnly(h)
      else     -> fetchAuto(h, preferProxy)
    } ?: return emptyList()
    return parse(doc, h, per)
  }

  suspend fun fetchMany(handles: List<String>, per: Int, mode: String, preferProxy: Boolean): List<TgPost> {
    val out = ArrayList<TgPost>()
    for (h in handles) {
      runCatching { fetchChannel(h, per, mode, preferProxy) }
        .onSuccess { out.addAll(it) }
    }
    return out
  }
}

package com.secondmind.minimal.feature.news

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object NewsRepository {
    private data class Entry(val at: Long, val items: List<Article>)
    private val cache = HashMap<NewsTab, Entry>()
    private const val TTL_MS = 10 * 60 * 1000 // 10 minutes

    suspend fun get(tab: NewsTab, force: Boolean = false): List<Article> = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        if (!force) {
            val e = cache[tab]
            if (e != null && now - e.at < TTL_MS) return@withContext e.items
        }
        val fresh = NewsApi.fetch(tab)
        cache[tab] = Entry(now, fresh)
        return@withContext fresh
    }
}

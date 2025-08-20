package com.secondmind.minimal.feature.youtube

data class YtItem(val id: String, val url: String)
data class YtMeta(val id: String, val title: String, val channel: String, val thumbUrl: String)

object YtId {
    private val patterns = listOf(
        Regex("""youtu\.be/([A-Za-z0-9_-]{6,})"""),
        Regex("""youtube\.com/watch\?[^#]*v=([A-Za-z0-9_-]{6,})"""),
        Regex("""youtube\.com/shorts/([A-Za-z0-9_-]{6,})""")
    )
    fun extract(from: String): String? {
        for (r in patterns) r.find(from)?.let { return it.groupValues[1] }
        return if (from.matches(Regex("""^[A-Za-z0-9_-]{6,}$"""))) from else null
    }
    fun canonicalUrl(id: String) = "https://www.youtube.com/watch?v=$id"
}

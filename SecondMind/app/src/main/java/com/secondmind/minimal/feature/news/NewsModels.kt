package com.secondmind.minimal.feature.news

data class NewsSource(val id: String?, val name: String?)
data class Article(
    val title: String?,
    val url: String?,
    val urlToImage: String?,
    val source: NewsSource?,
    val publishedAt: String?
)
enum class NewsTab { ForYou, Tech, Markets, World, Sports, Crypto }

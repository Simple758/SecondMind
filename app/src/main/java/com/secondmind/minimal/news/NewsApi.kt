package com.secondmind.minimal.news

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal data class NewsSource(val id: String?, val name: String?)
internal data class NewsArticle(
    val source: NewsSource?,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
)
internal data class TopHeadlinesResponse(
    val status: String?,
    val totalResults: Int?,
    val articles: List<NewsArticle>?
)

internal interface NewsApi {
    @GET("/v2/top-headlines")
    suspend fun top(
        ("category") category: String? = null,
        ("q") q: String? = null,
        ("country") country: String = "us",
        ("apiKey") apiKey: String
    ) category: String? = null,
        ("q") q: String? = null,
        ("country") country: String = "us",
        ("apiKey") apiKey: String
    ): TopHeadlinesResponse
}

internal fun newsApi(): NewsApi = Retrofit.Builder()
    .baseUrl("https://newsapi.org")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(NewsApi::class.java)

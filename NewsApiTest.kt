package com.secondmind.minimal.news

import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    println("==> Testing NewsApi.fetchTopHeadlines() ...")
    try {
        val headlines = NewsApi.fetchTopHeadlines()
        if (headlines.isEmpty()) {
            println("⚠️ No headlines returned (empty list).")
        } else {
            headlines.take(5).forEachIndexed { i, item ->
                println("${i+1}. ${item.title}")
            }
        }
    } catch (t: Throwable) {
        println("❌ Error: ${t.message}")
        t.printStackTrace()
    }
}

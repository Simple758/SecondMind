package com.secondmind.minimal.core.ai

import android.content.Context

data class Prompt(
    val system: String? = null,
    val user: String
)

data class AIOptions(
    val model: String? = null,
    val temperature: Double? = null,
    val maxTokens: Int? = null,
    val endpointPath: String? = null, // e.g. "/v1/chat/completions"
    val stream: Boolean = false
)

sealed class AIResult {
    data class Text(val content: String, val raw: String? = null) : AIResult()
    data class Error(val message: String, val cause: Throwable? = null) : AIResult()
}

interface AIProvider {
    val id: String
    suspend fun complete(
        context: Context,
        prompt: Prompt,
        options: AIOptions = AIOptions(),
        packet: ContextPacket = ContextPacket()
    ): AIResult
}

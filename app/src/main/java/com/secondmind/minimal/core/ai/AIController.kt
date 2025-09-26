package com.secondmind.minimal.core.ai

import android.content.Context

class AIController(
    private val providers: Map<String, AIProvider>,
    private val defaultProviderId: String = "deepseek"
) {
    private fun providerOrThrow(id: String?) =
        providers[id ?: defaultProviderId] ?: error("AI provider not configured: ${id ?: defaultProviderId}")

    suspend fun complete(
        context: Context,
        prompt: Prompt,
        options: AIOptions = AIOptions(),
        packet: ContextPacket = ContextPacket(),
        providerId: String? = null
    ): AIResult = providerOrThrow(providerId).complete(context, prompt, options, packet)
}

/** Simple service locator; call once (e.g., in Application.onCreate). */
object AIServiceLocator {
    @Volatile private var controller: AIController? = null

    /** Default to DeepSeek; swap baseUrl for Termux/local servers. */
    fun initDeepSeek(baseUrl: String = "https://api.deepseek.com"): AIController {
        val deepseek = DeepSeekProvider(baseUrl = baseUrl)
        val map = mapOf(deepseek.id to deepseek)
        controller = AIController(map)
        return controller!!
    }

    fun get(): AIController = controller ?: initDeepSeek()
}

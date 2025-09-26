package com.secondmind.minimal.core.ai

import android.content.Context

/** Example: summarize a notification with DeepSeek via AIController. */
object NotificationSummarizer {
    suspend fun summarize(
        context: Context,
        appPackage: String?,
        text: String
    ): AIResult {
        val packet = ContextPacket.fromNotification(appPkg = appPackage, text = text, extras = null, ctx = context)
        val prompt = Prompt(
            system = "Summarize the notification concisely for a quick glance.",
            user = text
        )
        return AIServiceLocator.get().complete(
            context = context,
            prompt = prompt,
            options = AIOptions(model = "deepseek-chat", maxTokens = 120, temperature = 0.2),
            packet = packet
        )
    }
}

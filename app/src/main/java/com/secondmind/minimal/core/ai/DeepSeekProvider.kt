package com.secondmind.minimal.core.ai

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

interface APIKeyProvider { suspend fun get(context: Context): String? }

/** Reads DEEPSEEK_API_KEY from BuildConfig or env (works with Termux). */
class EnvOrBuildConfigKeyProvider(
    private val envVar: String = "DEEPSEEK_API_KEY",
    private val buildConfigFieldName: String = "DEEPSEEK_API_KEY"
) : APIKeyProvider {
    override suspend fun get(context: Context): String? {
        val viaBuildConfig = try {
            val cls = Class.forName(context.packageName + ".BuildConfig")
            val field = cls.getDeclaredField(buildConfigFieldName)
            field.isAccessible = true
            field.get(null)?.toString()
        } catch (_: Throwable) { null }
        if (!viaBuildConfig.isNullOrBlank()) return viaBuildConfig
        return try { System.getenv(envVar) } catch (_: Throwable) { null }
    }
}

/** OpenAI-compatible JSON; works with DeepSeek cloud and local compatible servers. */
class DeepSeekProvider(
    private val baseUrl: String,
    private val defaultModel: String = "deepseek-chat",
    private val apiKeyProvider: APIKeyProvider = EnvOrBuildConfigKeyProvider(),
    private val openAICompatiblePath: String = "/v1/chat/completions"
) : AIProvider {

    override val id: String = "deepseek"

    override suspend fun complete(
        context: Context,
        prompt: Prompt,
        options: AIOptions,
        packet: ContextPacket
    ): AIResult = withContext(Dispatchers.IO) {
        val key = apiKeyProvider.get(context)
        val endpoint = baseUrl.trimEnd('/') + (options.endpointPath ?: openAICompatiblePath)

        // Privacy redaction
        val redacted = PolicyEngine.apply(packet)

        // Compose messages
        val messages = JSONArray()
        prompt.system?.takeIf { it.isNotBlank() }?.let {
            messages.put(JSONObject().put("role", "system").put("content", it))
        }
        buildString {
            append("You are an assistant inside an Android app.\n")
            redacted.text?.let { append("Context: ").append(it).append('\n') }
            redacted.appPackage?.let { append("App: ").append(it).append('\n') }
            if (redacted.source != ContextPacket.Source.Unknown) append("Source: ${redacted.source}\n")
        }.takeIf { it.isNotBlank() }?.let {
            messages.put(JSONObject().put("role", "system").put("content", it))
        }
        messages.put(JSONObject().put("role", "user").put("content", prompt.user))

        val body = JSONObject()
            .put("model", options.model ?: defaultModel)
            .put("messages", messages)
            .put("temperature", options.temperature ?: 0.2)
            .put("stream", false)
        options.maxTokens?.let { body.put("max_tokens", it) }

        val req = Request.Builder()
            .url(endpoint)
            .post(body.toString().toRequestBody("application/json; charset=utf-8".toMediaType()))
            .apply { if (!key.isNullOrBlank()) header("Authorization", "Bearer $key") }
            .build()

        val call = Http.client.newCall(req)
        return@withContext try {
            call.execute().use { resp ->
                val raw = resp.body?.string().orEmpty()
                if (!resp.isSuccessful) return@use AIResult.Error("HTTP ${resp.code}: ${resp.message}\n$raw", null)
                val json = JSONObject(raw)
                val content = json.optJSONArray("choices")
                    ?.optJSONObject(0)
                    ?.optJSONObject("message")
                    ?.optString("content")
                    ?: json.optString("text") // some servers return plain text
                AIResult.Text(content = content.ifBlank { raw }, raw = raw)
            }
        } catch (t: Throwable) {
            AIResult.Error("Network/parse error: ${t.message}", t)
        }
    }
}

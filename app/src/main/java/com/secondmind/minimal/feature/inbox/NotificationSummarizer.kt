package com.secondmind.minimal.feature.inbox

import android.content.Context
import com.secondmind.minimal.core.ai.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object NotificationSummarizer {
  /** Very small, local fallback digest by app. */
  fun summarizeLocal(lines: List<String>): String {
    if (lines.isEmpty()) return "No recent notifications."
    val byApp = lines.groupBy { it.substringBefore(":").ifBlank { "app" } }
    val sb = StringBuilder()
    for ((app, lns) in byApp) {
      val sample = lns.take(3).joinToString("; ") { it.substringAfter(":").take(80) }
      sb.append("• ").append(app).append(": ").append(sample)
      if (lns.size > 3) sb.append(" …(+").append(lns.size - 3).append(")")
      sb.append('\n')
    }
    return sb.toString().trim()
  }

  /** Optional AI refine (120 tokens max). Falls back to local on error. */
  suspend fun summarizeWithAI(ctx: Context, lines: List<String>): String = withContext(Dispatchers.IO) {
    val local = summarizeLocal(lines)
    if (lines.isEmpty()) return@withContext local
    return@withContext try {
      val promptText = buildString {
        append("You receive a redacted snapshot of Android notifications grouped by app.\n")
        append("Return a tight, privacy-safe digest in 3–5 bullets. No PII. Max 120 tokens.\n")
        append("Lines:\n")
        for (ln in lines) append("- ").append(ln.take(200)).append('\n')
      }
      when (val res = AIServiceLocator.get().complete(
        context = ctx,
        prompt = Prompt(system = "Summarize briefly, privacy-first. Keep generic.", user = promptText),
        options = AIOptions(model = "deepseek-chat", maxTokens = 120, temperature = 0.2),
        packet = ContextPacket(source = ContextPacket.Source.UI, appPackage = ctx.packageName, text = "inbox-digest")
      )) {
        is AIResult.Text -> res.content
        is AIResult.Error -> "AI error: ${res.message}\n\n$local"
      }
    } catch (_: Throwable) {
      local
    }
  }
}

package com.secondmind.minimal.core.ai

import android.content.Context
import android.os.Bundle

data class ContextPacket(
    val source: Source = Source.Unknown,
    val appPackage: String? = null,
    val locale: String? = null,
    val timezone: String? = null,
    val text: String? = null,
    val extras: Map<String, String> = emptyMap()
) {
    enum class Source { Accessibility, Notification, Clipboard, UI, Unknown }

    companion object {
        fun fromNotification(appPkg: String?, text: String?, extras: Bundle? = null, ctx: Context? = null): ContextPacket {
            val loc = ctx?.resources?.configuration?.locales?.get(0)?.toLanguageTag()
            val tz = try { java.util.TimeZone.getDefault().id } catch (_: Throwable) { null }
            val ex = mutableMapOf<String, String>()
            extras?.keySet()?.forEach { key -> extras.get(key)?.let { ex[key] = it.toString() } }
            return ContextPacket(Source.Notification, appPkg, loc, tz, text, ex)
        }
    }
}

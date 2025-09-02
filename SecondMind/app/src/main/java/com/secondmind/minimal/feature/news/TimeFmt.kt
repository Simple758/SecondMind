package com.secondmind.minimal.feature.news

import java.time.OffsetDateTime
import java.time.Duration
import java.time.format.DateTimeParseException

fun timeAgo(iso: String?): String {
    if (iso.isNullOrBlank()) return ""
    return try {
        val t = OffsetDateTime.parse(iso)
        val d = Duration.between(t, OffsetDateTime.now())
        val mins = d.toMinutes()
        when {
            mins < 1 -> "just now"
            mins < 60 -> "${mins}m ago"
            mins < 60*24 -> "${mins/60}h ago"
            else -> "${mins/1440}d ago"
        }
    } catch (_: DateTimeParseException) {
        ""
    }
}

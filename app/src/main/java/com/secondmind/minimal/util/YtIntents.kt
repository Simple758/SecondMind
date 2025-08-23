package com.secondmind.minimal.util

import android.content.Context
import android.content.Intent
import android.net.Uri

object YtIntents {
  private const val REVANCED = "app.revanced.android.youtube"
  private const val YT_OFFICIAL = "com.google.android.youtube"

  private fun extractVideoId(raw: String): String? {
    val url = raw.trim()
    val rx = Regex(
      "(?i)(?:v=|youtu\\.be/|/shorts/|/live/|/embed/)([A-Za-z0-9_-]{6,})"
    )
    return rx.find(url)?.groupValues?.getOrNull(1)
  }

  private fun tryStart(ctx: Context, intent: Intent): Boolean =
    try { ctx.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); true } catch (_: Throwable) { false }

  /** Open a YouTube link in ReVanced if possible; if not, search for it. Falls back to official YouTube, then browser. */
  fun openYouTubeSmart(ctx: Context, urlOrQuery: String) {
    val id = extractVideoId(urlOrQuery)
    val httpsUrl = "https://www.youtube.com/watch?v=${id ?: ""}".trimEnd('=')

    // 1) ReVanced: deep link by id
    if (id != null && tryStart(ctx, Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id")).setPackage(REVANCED))) return
    //    ReVanced: https
    if (id != null && tryStart(ctx, Intent(Intent.ACTION_VIEW, Uri.parse(httpsUrl)).setPackage(REVANCED))) return
    //    ReVanced: search
    if (tryStart(ctx, Intent(Intent.ACTION_SEARCH).setPackage(REVANCED).putExtra("query", urlOrQuery))) return
    if (tryStart(ctx, Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query="+Uri.encode(urlOrQuery))).setPackage(REVANCED))) return

    // 2) Official YouTube
    if (id != null && tryStart(ctx, Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id")).setPackage(YT_OFFICIAL))) return
    if (id != null && tryStart(ctx, Intent(Intent.ACTION_VIEW, Uri.parse(httpsUrl)).setPackage(YT_OFFICIAL))) return
    if (tryStart(ctx, Intent(Intent.ACTION_SEARCH).setPackage(YT_OFFICIAL).putExtra("query", urlOrQuery))) return
    if (tryStart(ctx, Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query="+Uri.encode(urlOrQuery))).setPackage(YT_OFFICIAL))) return

    // 3) Generic (browser or any handler)
    tryStart(ctx, Intent(Intent.ACTION_VIEW, Uri.parse(if (id != null) httpsUrl else urlOrQuery)))
  }
}

package com.secondmind.minimal.util

import android.content.Context
import android.content.Intent
import android.net.Uri

object YtIntents {
  private const val REVANCED = "app.revanced.android.youtube"
  private const val YT_OFFICIAL = "com.google.android.youtube"

  /** Try ReVanced first, then official YouTube, then any handler (browser). */
  fun openYouTubePreferred(ctx: Context, url: String) {
    val u = try { Uri.parse(url) } catch (_: Throwable) { null } ?: return
    // ReVanced
    try {
      val i = Intent(Intent.ACTION_VIEW, u).setPackage(REVANCED).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      ctx.startActivity(i); return
    } catch (_: Throwable) {}
    // Official YouTube
    try {
      val i = Intent(Intent.ACTION_VIEW, u).setPackage(YT_OFFICIAL).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      ctx.startActivity(i); return
    } catch (_: Throwable) {}
    // Generic
    try {
      ctx.startActivity(Intent(Intent.ACTION_VIEW, u).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    } catch (_: Throwable) {}
  }
}

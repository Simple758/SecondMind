package com.secondmind.minimal.diag

import android.content.Context

object NotifDiag {
  private const val PREF = "notif_diag"
  private const val K_CONNECTED_AT = "connected_at"
  private const val K_POSTED_COUNT = "posted_count"

  private fun pref(ctx: Context) =
    ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)

  fun markConnected(ctx: Context) {
    pref(ctx).edit().putLong(K_CONNECTED_AT, System.currentTimeMillis()).apply()
  }
  fun markPosted(ctx: Context) {
    val p = pref(ctx)
    val c = p.getInt(K_POSTED_COUNT, 0) + 1
    p.edit().putInt(K_POSTED_COUNT, c).apply()
  }

  fun connectedAt(ctx: Context): Long = pref(ctx).getLong(K_CONNECTED_AT, 0L)
  fun postedCount(ctx: Context): Int = pref(ctx).getInt(K_POSTED_COUNT, 0)
}

package com.secondmind.minimal.diag

import android.content.Context

object NotifDiag {
  private const val SP = "sm_notif_diag"

  fun markConnected(ctx: Context) {
    ctx.getSharedPreferences(SP, 0)
      .edit()
      .putLong("connected_at", System.currentTimeMillis())
      .apply()
  }

  fun bumpPosted(ctx: Context) {
    val sp = ctx.getSharedPreferences(SP, 0)
    val n = sp.getLong("posted", 0L) + 1L
    sp.edit().putLong("posted", n).apply()
  }

  fun connectedAt(ctx: Context): Long =
    ctx.getSharedPreferences(SP, 0).getLong("connected_at", 0L)

  fun postedCount(ctx: Context): Long =
    ctx.getSharedPreferences(SP, 0).getLong("posted", 0L)
}

package com.secondmind.minimal.future.engine

import android.content.Context
import com.secondmind.minimal.future.db.FutureRepos
import com.secondmind.minimal.future.db.ModeStateEntity
import java.util.Calendar

object ModeEngine {
  fun infer(nowMillis: Long = System.currentTimeMillis()): String {
    val cal = Calendar.getInstance().apply { timeInMillis = nowMillis }
    val h = cal.get(Calendar.HOUR_OF_DAY)
    // Trader window 07:00–22:00
    if (h in 7..22) return "Trader"
    return "Everyday"
  }

  suspend fun ensureState(ctx: Context) {
    val repo = FutureRepos(ctx)
    val cur = repo.modes.load()
    if (cur == null) {
      repo.modes.upsert(ModeStateEntity(mode = infer()))
    }
  }
}

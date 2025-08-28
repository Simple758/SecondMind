package com.secondmind.minimal.future.engine

import android.content.Context
import com.secondmind.minimal.future.db.*

object SeedEvaluator {
  /**
   * Offline v0.1 evaluator:
   * - TIME: fire when deadline <= now
   * - KEYWORD: placeholder (no-op)
   * - PRICE_MANUAL: placeholder (no-op)
   */
  suspend fun runOnce(ctx: Context) {
    val repo = FutureRepos(ctx)
    val now = System.currentTimeMillis()
    val pending = repo.seeds.byStatus("PENDING")
    for (s in pending) {
      val shouldFire = when (s.type) {
        "time" -> s.deadline > 0 && s.deadline <= now
        else -> false
      }
      if (shouldFire) {
        repo.signals.insert(
          SignalEntity(
            title = "Seed fired: ${s.type}",
            source = "seed",
            priority = 1,
            dueAt = s.deadline
          )
        )
        repo.seeds.updateStatus(s.id, "FIRED")
      }
    }
  }
}

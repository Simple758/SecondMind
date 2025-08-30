package com.secondmind.minimal.v01.domain

import com.secondmind.minimal.v01.data.entities.UserSignal
import com.secondmind.minimal.v01.data.entities.UserSignalType
import java.util.Calendar

object ModeEngine {
    /** Returns "Trader", "Cosmic", or "Everyday" based on simple heuristics. */
    fun inferMode(nowMillis: Long, recentSignals: List<UserSignal>): String {
        val cal = Calendar.getInstance().apply { timeInMillis = nowMillis }
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val traderByTime = hour in 7..22
        val traderBySignal = recentSignals.any {
            it.type == UserSignalType.APP_USED && it.value.contains("trading", ignoreCase = true)
        }
        if (traderByTime || traderBySignal) return "Trader"

        val cosmicCount = recentSignals.count {
            (it.type == UserSignalType.KEYWORD && it.value.contains("astro", ignoreCase = true))
        }
        val cosmicRecent = recentSignals.take(10).any { it.value.contains("astro", ignoreCase = true) }
        if (cosmicCount >= 2 || cosmicRecent) return "Cosmic"

        return "Everyday"
    }
}

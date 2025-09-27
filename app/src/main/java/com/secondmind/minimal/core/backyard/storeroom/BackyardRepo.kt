package com.secondmind.minimal.core.backyard.storeroom
import java.util.concurrent.ConcurrentHashMap
/** Simple in-memory repo stub (Room later). */
class BackyardRepo {
  private val facts = ConcurrentHashMap<String, Fact>()
  fun upsertFact(f: Fact) { facts[f.key] = f }
  fun getFact(key: String): Fact? = facts[key]
  fun list(prefix: String): List<Fact> = facts.values.filter { it.key.startsWith(prefix) }.sortedByDescending { it.ts }
  fun cleanup(now: Long = System.currentTimeMillis()) {
    facts.values.removeIf { f -> f.ttlDays > 0 && now - f.ts > f.ttlDays * 86_400_000L }
  }
}

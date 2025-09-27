package com.secondmind.minimal.core.backyard.storeroom
/** Curated, sanitized records AI may read via gateway. */
data class Fact(val key: String, val value: String, val ts: Long, val ttlDays: Int = 30)
data class Event(val id: String, val type: String, val payload: String, val ts: Long)
data class Insight(val id: String, val tag: String, val summary: String, val ts: Long)

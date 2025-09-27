package com.secondmind.minimal.core.backyard.reception
/** Tiny in-memory ring buffer, auto-truncating; ephemeral. */
class RingBuffer<T>(private val cap: Int = 128) {
  private val q = ArrayList<T>()
  @Synchronized fun add(x: T) { if (q.size >= cap) q.removeAt(0); q.add(x) }
  @Synchronized fun snapshot(): List<T> = q.toList()
  @Synchronized fun clear() { q.clear() }
}

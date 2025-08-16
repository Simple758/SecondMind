package com.secondmind.minimal

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private class InMemoryNotes : NotesRepository {
  private val lock = Mutex()
  private val list = mutableListOf<Note>()
  private var seq = 1L

  override suspend fun add(text: String): Long = lock.withLock {
    val id = seq++; list += Note(id, text, System.currentTimeMillis()); id
  }
  override suspend fun list(limit: Int): List<Note> = lock.withLock {
    list.takeLast(limit).reversed()
  }
  override suspend fun count(): Int = lock.withLock { list.size }
}

/** Loaded reflectively from MainActivity to set Providers.notes */
@Suppress("unused")
class FlavorInit {
  init { Providers.notes = InMemoryNotes() }
}

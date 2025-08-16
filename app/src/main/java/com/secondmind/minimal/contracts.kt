package com.secondmind.minimal

data class Note(val id: Long, val text: String, val createdAt: Long)

interface NotesRepository {
  suspend fun add(text: String): Long
  suspend fun list(limit: Int = 100): List<Note>
  suspend fun count(): Int
}

/** Implemented in flavor source sets. */
object Providers {
  lateinit var notes: NotesRepository
}

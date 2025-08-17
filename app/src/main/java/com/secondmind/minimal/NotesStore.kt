package com.secondmind.minimal
object NotesStore {
  private val list = mutableListOf<String>()
  fun add(t: String) { if (t.isNotBlank()) list += t.trim() }
  fun count(): Int = list.size
}

package com.secondmind.minimal.v01.data.repo

import com.secondmind.minimal.v01.data.dao.NoteDao
import com.secondmind.minimal.v01.data.entities.Note

class NoteRepository(private val dao: NoteDao) {
    suspend fun add(text: String, tagsCsv: String = "", isSeed: Boolean = false): Long =
        dao.insert(Note(text = text, tagsCsv = tagsCsv, isSeed = isSeed))
    suspend fun list(): List<Note> = dao.list()
}

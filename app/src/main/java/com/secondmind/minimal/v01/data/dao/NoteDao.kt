package com.secondmind.minimal.v01.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.secondmind.minimal.v01.data.entities.Note

@Dao
interface NoteDao {
    @Insert suspend fun insert(note: Note): Long
    @Update suspend fun update(note: Note)
    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    suspend fun list(): List<Note>
}

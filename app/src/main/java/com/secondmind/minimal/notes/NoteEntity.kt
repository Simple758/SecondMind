package com.secondmind.minimal.notes
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "notes")
data class NoteEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val text: String,
  val createdAt: Long = System.currentTimeMillis(),
  val source: String = "manual",
  val favorite: Boolean = false
)

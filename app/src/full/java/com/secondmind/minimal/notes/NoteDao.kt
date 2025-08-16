package com.secondmind.minimal.notes
import androidx.room.*
import kotlinx.coroutines.flow.Flow
@Dao
interface NoteDao {
  @Insert suspend fun insert(note: NoteEntity)
  @Query("SELECT * FROM notes ORDER BY createdAt DESC LIMIT :limit")
  fun recent(limit: Int): Flow<List<NoteEntity>>
  @Query("SELECT COUNT(*) FROM notes")
  fun count(): Flow<Int>
}

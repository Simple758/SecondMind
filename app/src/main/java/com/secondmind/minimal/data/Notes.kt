package com.secondmind.minimal.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Dao
interface NoteDao {
    @Query("SELECT COUNT(*) FROM notes")
    fun count(): Flow<Int>

    @Query("SELECT * FROM notes ORDER BY createdAt DESC LIMIT :limit")
    fun recent(limit: Int): Flow<List<NoteEntity>>

    @Insert
    suspend fun insert(note: NoteEntity)
}

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class NotesDb : RoomDatabase() {
    abstract fun notes(): NoteDao
    companion object {
        @Volatile private var INSTANCE: NotesDb? = null
        fun get(context: Context): NotesDb =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    NotesDb::class.java,
                    "notes.db"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
    }
}

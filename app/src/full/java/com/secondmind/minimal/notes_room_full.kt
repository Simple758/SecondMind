package com.secondmind.minimal

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Entity(tableName = "notes")
data class NoteEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val text: String,
  val createdAt: Long
)

@Dao
interface NoteDao {
  @Insert suspend fun insert(e: NoteEntity): Long
  @Query("SELECT * FROM notes ORDER BY createdAt DESC LIMIT :limit")
  suspend fun recent(limit: Int): List<NoteEntity>
  @Query("SELECT COUNT(*) FROM notes") suspend fun count(): Int
}

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class AppDb : RoomDatabase() {
  abstract fun notes(): NoteDao
  companion object {
    @Volatile private var INSTANCE: AppDb? = null
    fun get(ctx: Context): AppDb =
      INSTANCE ?: synchronized(this) {
        INSTANCE ?: Room.databaseBuilder(ctx, AppDb::class.java, "sm.db").build()
          .also { INSTANCE = it }
      }
  }
}

private class RoomNotesRepository(ctx: Context) : NotesRepository {
  private val dao = AppDb.get(ctx).notes()
  override suspend fun add(text: String): Long = withContext(Dispatchers.IO) {
    dao.insert(NoteEntity(text = text, createdAt = System.currentTimeMillis()))
  }
  override suspend fun list(limit: Int): List<Note> = withContext(Dispatchers.IO) {
    dao.recent(limit).map { Note(it.id, it.text, it.createdAt) }
  }
  override suspend fun count(): Int = withContext(Dispatchers.IO) { dao.count() }
}

/** Loaded reflectively from MainActivity to set Providers.notes */
@Suppress("unused")
class FlavorInit {
  init {
    val app = try {
      Class.forName("android.app.AppGlobals")
        .getMethod("getInitialApplication")
        .invoke(null) as android.app.Application
    } catch (_: Throwable) { null }
    requireNotNull(app) { "App context not ready" }
    Providers.notes = RoomNotesRepository(app)
  }
}

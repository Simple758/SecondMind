package com.secondmind.minimal.future.db

import androidx.room.*

@Entity(tableName = "notes")
data class NoteEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val text: String,
  val tagsCsv: String = "",
  val createdAt: Long = System.currentTimeMillis(),
  val updatedAt: Long = System.currentTimeMillis(),
  val isSeed: Boolean = false
)

@Entity(
  tableName = "seeds",
  foreignKeys = [ForeignKey(
    entity = NoteEntity::class,
    parentColumns = ["id"],
    childColumns = ["noteId"],
    onDelete = ForeignKey.CASCADE
  )],
  indices = [Index("status"), Index("deadline"), Index("noteId")]
)
data class SeedEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val noteId: Long,
  val type: String,      // time | price_manual | keyword | context
  val operator: String = "==",
  val value: String = "",
  val keyword: String = "",
  val deadline: Long = 0L,
  val status: String = "PENDING" // PENDING | FIRED | ACKED
)

@Entity(
  tableName = "signals",
  indices = [Index("priority"), Index("dueAt")]
)
data class SignalEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val title: String,
  val source: String = "seed",
  val priority: Int = 0,
  val dueAt: Long = 0L,
  val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "mode_state")
data class ModeStateEntity(
  @PrimaryKey val id: Int = 1,
  val mode: String = "Everyday",
  val lastAutoSwitch: Long = 0L,
  val lastManualMode: String? = null
)

@Entity(
  tableName = "user_signals",
  indices = [Index("timestamp")]
)
data class UserSignalEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val type: String,
  val value: String = "",
  val timestamp: Long = System.currentTimeMillis()
)

@Dao
interface NoteDao {
  @Insert suspend fun insert(n: NoteEntity): Long
  @Query("SELECT * FROM notes ORDER BY updatedAt DESC LIMIT :limit")
  suspend fun recent(limit: Int = 20): List<NoteEntity>
}

@Dao
interface SeedDao {
  @Insert suspend fun insert(s: SeedEntity): Long
  @Query("SELECT * FROM seeds WHERE status = :status ORDER BY deadline ASC")
  suspend fun byStatus(status: String = "PENDING"): List<SeedEntity>
  @Query("UPDATE seeds SET status=:status WHERE id=:id")
  suspend fun updateStatus(id: Long, status: String)
  @Query("SELECT * FROM seeds ORDER BY id DESC LIMIT :limit")
  suspend fun list(limit: Int = 50): List<SeedEntity>
}

@Dao
interface SignalDao {
  @Insert suspend fun insert(s: SignalEntity): Long
  @Query("SELECT * FROM signals ORDER BY priority DESC, dueAt ASC, createdAt ASC LIMIT :limit")
  suspend fun top(limit: Int = 10): List<SignalEntity>
  @Query("DELETE FROM signals WHERE id=:id") suspend fun delete(id: Long)
}

@Dao
interface ModeDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsert(m: ModeStateEntity)
  @Query("SELECT * FROM mode_state WHERE id=1") suspend fun load(): ModeStateEntity?
}

@Dao
interface UserSignalDao {
  @Insert suspend fun insert(s: UserSignalEntity): Long
  @Query("SELECT * FROM user_signals ORDER BY timestamp DESC LIMIT :limit")
  suspend fun recent(limit: Int = 50): List<UserSignalEntity>
}

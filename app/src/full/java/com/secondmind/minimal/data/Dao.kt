package com.secondmind.minimal.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun upsert(e: NotificationEntity)

  @Query("UPDATE notifications SET removedAt=:t WHERE key=:key")
  suspend fun markRemoved(key: String, t: Long)

  @Query("SELECT * FROM notifications ORDER BY postedAt DESC")
  fun streamAll(): Flow<List<NotificationEntity>>

  @Query("SELECT * FROM notifications WHERE id = :id")
  fun streamById(id: Long): Flow<NotificationEntity?>

  @Query("DELETE FROM notifications WHERE id = :id")
  suspend fun deleteById(id: Long)

  @Query("DELETE FROM notifications")
  suspend fun clearAll()

  @Query("DELETE FROM notifications WHERE postedAt < :cutoff")
  suspend fun pruneOlderThan(cutoff: Long)
}

package com.secondmind.minimal.v01.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.secondmind.minimal.v01.data.entities.Signal

@Dao
interface SignalDao {
    @Insert suspend fun insert(signal: Signal): Long
    @Query("SELECT * FROM signals ORDER BY priority DESC, dueAt ASC, createdAt ASC LIMIT 3")
    suspend fun top3(): List<Signal>
}

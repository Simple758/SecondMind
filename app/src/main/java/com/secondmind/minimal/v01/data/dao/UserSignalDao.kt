package com.secondmind.minimal.v01.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.secondmind.minimal.v01.data.entities.UserSignal

@Dao
interface UserSignalDao {
    @Insert suspend fun insert(signal: UserSignal): Long
    @Query("SELECT * FROM user_signals ORDER BY timestamp DESC LIMIT 20")
    suspend fun latest(): List<UserSignal>
}

package com.secondmind.minimal.v01.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.secondmind.minimal.v01.data.entities.ModeState

@Dao
interface ModeStateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(state: ModeState)
    @Update suspend fun update(state: ModeState)
    @Query("SELECT * FROM mode_state WHERE id = 1")
    suspend fun get(): ModeState?
}

package com.secondmind.minimal.v01.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.secondmind.minimal.v01.data.entities.Seed
import com.secondmind.minimal.v01.data.entities.SeedStatus

@Dao
interface SeedDao {
    @Insert suspend fun insert(seed: Seed): Long
    @Update suspend fun update(seed: Seed)
    @Query("SELECT * FROM seeds WHERE status = :status")
    suspend fun byStatus(status: SeedStatus): List<Seed>
}

package com.secondmind.minimal.v01.data.repo

import com.secondmind.minimal.v01.data.dao.SeedDao
import com.secondmind.minimal.v01.data.entities.Seed
import com.secondmind.minimal.v01.data.entities.SeedStatus

class SeedRepository(private val dao: SeedDao) {
    suspend fun add(seed: Seed): Long = dao.insert(seed)
    suspend fun byStatus(status: SeedStatus) = dao.byStatus(status)
    suspend fun update(seed: Seed) = dao.update(seed)
}

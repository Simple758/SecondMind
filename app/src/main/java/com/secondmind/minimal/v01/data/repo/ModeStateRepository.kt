package com.secondmind.minimal.v01.data.repo

import com.secondmind.minimal.v01.data.dao.ModeStateDao
import com.secondmind.minimal.v01.data.entities.ModeState

class ModeStateRepository(private val dao: ModeStateDao) {
    suspend fun get(): ModeState? = dao.get()
    suspend fun upsert(state: ModeState) = dao.upsert(state)
}

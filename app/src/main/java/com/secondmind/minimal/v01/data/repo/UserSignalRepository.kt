package com.secondmind.minimal.v01.data.repo

import com.secondmind.minimal.v01.data.dao.UserSignalDao
import com.secondmind.minimal.v01.data.entities.UserSignal

class UserSignalRepository(private val dao: UserSignalDao) {
    suspend fun add(signal: UserSignal): Long = dao.insert(signal)
    suspend fun latest(): List<UserSignal> = dao.latest()
}

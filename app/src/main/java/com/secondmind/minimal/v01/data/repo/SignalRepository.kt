package com.secondmind.minimal.v01.data.repo

import com.secondmind.minimal.v01.data.dao.SignalDao
import com.secondmind.minimal.v01.data.entities.Signal

class SignalRepository(private val dao: SignalDao) {
    suspend fun top3(): List<Signal> = dao.top3()
    suspend fun add(signal: Signal): Long = dao.insert(signal)
}

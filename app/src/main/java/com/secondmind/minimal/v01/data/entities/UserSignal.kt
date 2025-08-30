package com.secondmind.minimal.v01.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_signals")
data class UserSignal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val type: UserSignalType,
    val value: String,
    val timestamp: Long = System.currentTimeMillis()
)

enum class UserSignalType { APP_USED, KEYWORD, TIME }

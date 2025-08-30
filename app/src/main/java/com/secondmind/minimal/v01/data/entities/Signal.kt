package com.secondmind.minimal.v01.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "signals")
data class Signal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String,
    val source: SignalSource,
    val priority: Int = 0,
    val dueAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)

enum class SignalSource { seed, reminder, activity }

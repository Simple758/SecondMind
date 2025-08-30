package com.secondmind.minimal.v01.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mode_state")
data class ModeState(
    @PrimaryKey val id: Int = 1,
    val mode: String = "Everyday",
    val lastAutoSwitch: Long = 0L,
    val lastManualMode: String? = null
)

package com.secondmind.minimal.v01.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "seeds")
data class Seed(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val noteId: Long,
    val type: SeedType,
    val operator: String? = null,
    val value: String? = null,
    val keyword: String? = null,
    val deadline: Long? = null,
    val status: SeedStatus = SeedStatus.PENDING
)

enum class SeedType { time, price_manual, keyword, context }
enum class SeedStatus { PENDING, FIRED, ACKED }

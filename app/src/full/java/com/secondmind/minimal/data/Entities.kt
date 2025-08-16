package com.secondmind.minimal.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
  tableName = "notifications",
  indices = [Index(value = ["key"], unique = true), Index(value = ["appPackage"]), Index(value=["postedAt"])]
)
data class NotificationEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val key: String,
  val appPackage: String,
  val title: String?,
  val text: String?,
  val channel: String?,
  val category: String?,
  val postedAt: Long,
  val removedAt: Long?,
  val source: String,
  val extras: String?,
  val favorite: Boolean = false
)

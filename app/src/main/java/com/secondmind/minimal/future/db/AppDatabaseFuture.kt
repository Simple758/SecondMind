package com.secondmind.minimal.future.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
  entities = [
    NoteEntity::class, SeedEntity::class, SignalEntity::class,
    ModeStateEntity::class, UserSignalEntity::class
  ],
  version = 1, exportSchema = false
)
abstract class AppDatabaseFuture : RoomDatabase() {
  abstract fun noteDao(): NoteDao
  abstract fun seedDao(): SeedDao
  abstract fun signalDao(): SignalDao
  abstract fun modeDao(): ModeDao
  abstract fun userSignalDao(): UserSignalDao

  companion object {
    @Volatile private var inst: AppDatabaseFuture? = null
    fun get(ctx: Context): AppDatabaseFuture =
      inst ?: synchronized(this) {
        inst ?: Room.databaseBuilder(
          ctx.applicationContext,
          AppDatabaseFuture::class.java,
          "future.db"
        ).fallbackToDestructiveMigration().build().also { inst = it }
      }
  }
}

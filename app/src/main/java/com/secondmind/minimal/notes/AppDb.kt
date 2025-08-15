package com.secondmind.minimal.notes
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class AppDb : RoomDatabase() {
  abstract fun noteDao(): NoteDao
  companion object {
    @Volatile private var I: AppDb? = null
    fun get(ctx: Context): AppDb =
      I ?: synchronized(this) {
        I ?: Room.databaseBuilder(ctx.applicationContext, AppDb::class.java, "secondmind.db")
          .fallbackToDestructiveMigration()
          .build().also { I = it }
      }
  }
}

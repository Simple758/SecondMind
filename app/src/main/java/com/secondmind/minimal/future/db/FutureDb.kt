package com.secondmind.minimal.future.db
import android.content.Context; import androidx.room.*
@Database(entities=[NoteEntity::class,SeedEntity::class,SignalEntity::class,ModeStateEntity::class,UserSignalEntity::class],version=1,exportSchema=false)
abstract class FutureDb:RoomDatabase(){
  abstract fun noteDao():NoteDao; abstract fun seedDao():SeedDao; abstract fun signalDao():SignalDao; abstract fun modeDao():ModeStateDao; abstract fun userSignalDao():UserSignalDao
  companion object{ @Volatile private var inst:FutureDb?=null
    fun get(ctx:Context)= inst?: synchronized(this){ inst?: Room.databaseBuilder(ctx.applicationContext,FutureDb::class.java,"future.db").fallbackToDestructiveMigration().build().also{inst=it} }
  }
}

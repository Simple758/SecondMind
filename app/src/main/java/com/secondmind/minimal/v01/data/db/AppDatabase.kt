package com.secondmind.minimal.v01.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.secondmind.minimal.v01.data.dao.*
import com.secondmind.minimal.v01.data.entities.*

@Database(
    entities = [Note::class, Seed::class, Signal::class, ModeState::class, UserSignal::class],
    version = 1, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun seedDao(): SeedDao
    abstract fun signalDao(): SignalDao
    abstract fun modeStateDao(): ModeStateDao
    abstract fun userSignalDao(): UserSignalDao
}

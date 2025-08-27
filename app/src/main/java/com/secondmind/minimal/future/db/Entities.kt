package com.secondmind.minimal.future.db
import androidx.room.*
@Entity(tableName="notes",indices=[Index("updatedAt")])
data class NoteEntity(@PrimaryKey(autoGenerate=true) val id:Long=0,text:String, val text:String=text, val tagsCsv:String?=null, val createdAt:Long=System.currentTimeMillis(), val updatedAt:Long=System.currentTimeMillis(), val isSeed:Boolean=false)
@Entity(tableName="seeds",indices=[Index(value=["status","deadline"])])
data class SeedEntity(@PrimaryKey(autoGenerate=true) val id:Long=0, val noteId:Long, val type:String, val operator:String?=null, val value:String?=null, val keyword:String?=null, val deadline:Long?=null, val status:String="PENDING")
@Entity(tableName="signals",indices=[Index(value=["priority","dueAt"])])
data class SignalEntity(@PrimaryKey(autoGenerate=true) val id:Long=0, val title:String, val source:String, val priority:Int=0, val dueAt:Long?=null, val createdAt:Long=System.currentTimeMillis())
@Entity(tableName="mode_state")
data class ModeStateEntity(@PrimaryKey val id:Int=1, val mode:String="Everyday", val lastAutoSwitch:Long=0, val lastManualMode:String?=null)
@Entity(tableName="user_signals")
data class UserSignalEntity(@PrimaryKey(autoGenerate=true) val id:Long=0, val type:String, val value:String?=null, val timestamp:Long=System.currentTimeMillis())

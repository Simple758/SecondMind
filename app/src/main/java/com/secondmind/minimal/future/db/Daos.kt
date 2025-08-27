package com.secondmind.minimal.future.db
import androidx.room.*; import kotlinx.coroutines.flow.Flow
@Dao interface NoteDao{
  @Insert fun insert(e:NoteEntity):Long
  @Update fun update(e:NoteEntity)
  @Query("SELECT * FROM notes WHERE id=:id") fun byId(id:Long):NoteEntity?
  @Query("SELECT * FROM notes ORDER BY updatedAt DESC LIMIT :limit") fun recent(limit:Int=20):Flow<List<NoteEntity>>
}
@Dao interface SeedDao{
  @Insert fun insert(e:SeedEntity):Long
  @Update fun update(e:SeedEntity)
  @Query("SELECT * FROM seeds WHERE status=:status") fun byStatus(status:String="PENDING"):List<SeedEntity>
  @Query("SELECT s.*, n.text AS _note FROM seeds s JOIN notes n ON n.id=s.noteId ORDER BY s.id DESC LIMIT :limit")
  fun latest(limit:Int=20):List<SeedWithNote>
}
data class SeedWithNote(@Embedded val seed:SeedEntity, @ColumnInfo(name="_note") val noteText:String)
@Dao interface SignalDao{
  @Insert fun insert(e:SignalEntity):Long
  @Update fun update(e:SignalEntity)
  @Query("SELECT * FROM signals ORDER BY priority DESC, (dueAt IS NULL) ASC, dueAt ASC, createdAt ASC LIMIT :limit")
  fun top(limit:Int=3):List<SignalEntity>
  @Query("DELETE FROM signals WHERE id=:id") fun deleteById(id:Long)
}
@Dao interface ModeStateDao{ @Insert(onConflict=OnConflictStrategy.REPLACE) fun upsert(e:ModeStateEntity); @Query("SELECT * FROM mode_state WHERE id=1") fun get():ModeStateEntity? }
@Dao interface UserSignalDao{ @Insert fun insert(e:UserSignalEntity):Long; @Query("SELECT * FROM user_signals WHERE type=:type ORDER BY timestamp DESC LIMIT :limit") fun latest(type:String,limit:Int=10):List<UserSignalEntity> }

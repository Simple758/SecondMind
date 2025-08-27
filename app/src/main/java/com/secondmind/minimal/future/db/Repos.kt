package com.secondmind.minimal.future.db

import android.content.Context
import kotlinx.coroutines.flow.Flow

class FutureRepos private constructor(ctx: Context) {
    private val db = FutureDb.get(ctx)
    val notes = db.noteDao()
    val seeds = db.seedDao()
    val signals = db.signalDao()
    val modes = db.modeDao()
    val user = db.userSignalDao()

    companion object {
        @Volatile private var inst: FutureRepos? = null
        fun get(ctx: Context): FutureRepos =
            inst ?: synchronized(this) {
                inst ?: FutureRepos(ctx.applicationContext).also { inst = it }
            }
    }
}

object FutureNoteRepo {
    fun add(
        ctx: Context,
        text: String,
        tags: String? = null,
        isSeed: Boolean = false
    ): Long {
        return FutureRepos.get(ctx).notes.insert(
            NoteEntity(text = text, tagsCsv = tags, isSeed = isSeed)
        )
    }

    fun recent(ctx: Context, limit: Int = 20): Flow<List<NoteEntity>> =
        FutureRepos.get(ctx).notes.recent(limit)
}

object FutureSeedRepo {
    fun addSeed(
        ctx: Context,
        noteId: Long,
        type: String,
        operator: String?,
        value: String?,
        keyword: String?,
        deadline: Long?,
        status: String = "PENDING"
    ): Long {
        return FutureRepos.get(ctx).seeds.insert(
            SeedEntity(
                noteId = noteId,
                type = type,
                operator = operator,
                value = value,
                keyword = keyword,
                deadline = deadline,
                status = status
            )
        )
    }

    fun pending(ctx: Context): List<SeedEntity> =
        FutureRepos.get(ctx).seeds.byStatus("PENDING")

    fun withNotes(ctx: Context, limit: Int = 20): List<SeedWithNote> =
        FutureRepos.get(ctx).seeds.latest(limit)

    fun update(ctx: Context, e: SeedEntity) {
        FutureRepos.get(ctx).seeds.update(e)
    }
}

object FutureSignalRepo {
    fun add(
        ctx: Context,
        title: String,
        source: String,
        priority: Int,
        dueAt: Long?
    ): Long {
        return FutureRepos.get(ctx).signals.insert(
            SignalEntity(title = title, source = source, priority = priority, dueAt = dueAt)
        )
    }

    fun top3(ctx: Context): List<SignalEntity> =
        FutureRepos.get(ctx).signals.top(3)
}

object FutureModeRepo {
    fun get(ctx: Context): ModeStateEntity {
        return FutureRepos.get(ctx).modes.get()
            ?: ModeStateEntity().also { FutureRepos.get(ctx).modes.upsert(it) }
    }

    fun set(ctx: Context, mode: String, manual: Boolean) {
        val cur = get(ctx)
        val upd = if (manual) {
            cur.copy(mode = mode, lastManualMode = mode)
        } else {
            cur.copy(mode = mode, lastAutoSwitch = System.currentTimeMillis())
        }
        FutureRepos.get(ctx).modes.upsert(upd)
    }
}

object FutureUserSignalRepo {
    fun log(ctx: Context, type: String, value: String? = null) {
        FutureRepos.get(ctx).user.insert(UserSignalEntity(type = type, value = value))
    }

    fun latest(ctx: Context, type: String, limit: Int = 10): List<UserSignalEntity> =
        FutureRepos.get(ctx).user.latest(type, limit)
}

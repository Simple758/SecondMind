package com.secondmind.minimal.future.db

import android.content.Context

class FutureRepos(ctx: Context) {
  private val db = AppDatabaseFuture.get(ctx)
  val notes = db.noteDao()
  val seeds = db.seedDao()
  val signals = db.signalDao()
  val modes = db.modeDao()
  val user = db.userSignalDao()
}

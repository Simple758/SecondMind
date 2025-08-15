package com.secondmind.minimal.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.secondmind.minimal.data.AppDb
import com.secondmind.minimal.data.Keys
import com.secondmind.minimal.data.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class CleanupWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
  override suspend fun doWork(): Result {
    val days = applicationContext.dataStore.data.map { it[Keys.RETENTION_DAYS] ?: 7 }.first()
    val cutoff = System.currentTimeMillis() - days * 24L * 60 * 60 * 1000
    AppDb.get(applicationContext).notificationDao().pruneOlderThan(cutoff)
    return Result.success()
  }
}

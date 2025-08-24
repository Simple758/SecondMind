package com.secondmind.minimal

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.secondmind.minimal.work.CleanupWorker
import java.util.concurrent.TimeUnit

class SecondMindApp : Application() {
  override fun onCreate() {
    com.secondmind.minimal.diag.CrashLogger.install(this)
    super.onCreate()
    val work = PeriodicWorkRequestBuilder<CleanupWorker>(1, TimeUnit.DAYS).build()
    WorkManager.getInstance(this).enqueueUniquePeriodicWork("cleanup", ExistingPeriodicWorkPolicy.KEEP, work)
  }
}

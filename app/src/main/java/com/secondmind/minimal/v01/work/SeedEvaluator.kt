package com.secondmind.minimal.v01.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class SeedEvaluator(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        // v0.1 stub — return immediately
        return Result.success()
    }
}

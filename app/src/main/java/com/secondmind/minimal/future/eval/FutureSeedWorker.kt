package com.secondmind.minimal.future.eval
import android.content.Context; import androidx.work.CoroutineWorker; import androidx.work.WorkerParameters
class FutureSeedWorker(appContext:Context,params:WorkerParameters):CoroutineWorker(appContext,params){
  override suspend fun doWork():Result= try{ FutureSeedEvaluator.evaluateNow(applicationContext); Result.success() } catch(_:Throwable){ Result.retry() }
}

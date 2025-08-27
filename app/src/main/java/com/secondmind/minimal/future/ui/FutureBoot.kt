package com.secondmind.minimal.future.ui
import androidx.compose.runtime.*; import androidx.compose.ui.platform.LocalContext; import androidx.work.*
@Composable
fun FutureBoot(){
  val ctx=LocalContext.current
  LaunchedEffect(Unit){
    val req=PeriodicWorkRequestBuilder<com.secondmind.minimal.future.eval.FutureSeedWorker>(java.time.Duration.ofHours(3)).build()
    WorkManager.getInstance(ctx).enqueueUniquePeriodicWork("UniqueWork_FutureSeed", ExistingPeriodicWorkPolicy.KEEP, req)
  }
}

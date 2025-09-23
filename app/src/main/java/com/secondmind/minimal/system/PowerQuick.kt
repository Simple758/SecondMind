package com.secondmind.minimal.system

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun RequestDangerousOnce(perms: Array<String>) {
  val context = LocalContext.current
  var pending by rememberSaveable { mutableStateOf(PowerPerms.missing(context, perms).toTypedArray()) }
  val launcher = rememberLauncherForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
  ) {
    pending = PowerPerms.missing(context, perms).toTypedArray()
  }
  LaunchedEffect(Unit) {
    if (pending.isNotEmpty()) launcher.launch(pending)
  }
}

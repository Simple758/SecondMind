package com.secondmind.minimal.ui
import com.secondmind.minimal.util.putParcelableExtraSafe

import android.accessibilityservice.AccessibilityService
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.secondmind.minimal.MyAccessibilityService

@Composable
fun DebugScreen(onBack: () -> Unit) {
  val ctx = LocalContext.current
  val pm = ctx.packageManager
  val comp = remember { ComponentName(ctx, MyAccessibilityService::class.java).flattenToString() }
  val resolved by remember {
    mutableStateOf(
      pm.queryIntentServices(Intent(AccessibilityService.SERVICE_INTERFACE), PackageManager.MATCH_ALL)
        .filter { it.serviceInfo?.packageName == ctx.packageName }
        .map { "${it.serviceInfo?.name ?: ""} (exported=${it.serviceInfo?.exported})" }
    )
  }
  val enabled by remember {
    mutableStateOf(
      (Settings.Secure.getString(ctx.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES) ?: "")
        .contains(comp)
    )
  }

  Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Text("Accessibility Debug", style = MaterialTheme.typography.titleLarge)
    Text("Component: $comp")
    Text("Resolved by PM: " + if (resolved.isEmpty()) "(none)" else resolved.joinToString())
    Text("Enabled in Settings: $enabled")
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      OutlinedButton(onClick = {
        val i = Intent("android.settings.ACCESSIBILITY_DETAILS_SETTINGS")
        i.putExtra("android.provider.extra.EXTRA_ACCESSIBILITY_COMPONENT_NAME", comp)
        ctx.startActivity(i)
      }) { Text("Open Service Page") }
      OutlinedButton(onClick = { ctx.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) }) { Text("Open Accessibility") }
    }
    OutlinedButton(onClick = onBack) { Text("Back") }
  }
}

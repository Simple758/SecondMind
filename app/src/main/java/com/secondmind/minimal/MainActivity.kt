@file:OptIn(ExperimentalMaterial3Api::class)

package com.secondmind.minimal

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.datastore.preferences.core.edit
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.secondmind.minimal.data.Keys
import com.secondmind.minimal.data.dataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ensureChannel()
    setContent {
      val mode by rememberThemeMode()
      val dark = when (mode) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
      }
      val scheme = if (dark) darkColorScheme() else lightColorScheme()
      MaterialTheme(colorScheme = scheme) { AppNav() }
    }
  }

  private fun ensureChannel() {
    if (Build.VERSION.SDK_INT >= 26) {
      val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
      nm.createNotificationChannel(NotificationChannel("sm", "SecondMind", NotificationManager.IMPORTANCE_DEFAULT))
    }
  }
}

@Composable
fun rememberThemeMode(): State<String> {
  val ctx = LocalContext.current
  val flow = remember { ctx.dataStore.data.map { it[Keys.THEME] ?: "system" } }
  return flow.collectAsState(initial = "system")
}

@Composable
fun AppNav() {
  val nav = rememberNavController()
  Scaffold(topBar = {
    CenterAlignedTopAppBar(title = { Text(titleFor(nav)) })
  }) { pad ->
    NavHost(navController = nav, startDestination = "home", modifier = Modifier.padding(pad)) {
      composable("home") { HomeScreen(onSettings = { nav.navigate("settings") }) }
      composable("settings") { SettingsScreen(onBack = { nav.popBackStack() }) }
    }
  }
}

@Composable
fun titleFor(nav: NavHostController): String {
  val e by nav.currentBackStackEntryAsState()
  return when (e?.destination?.route ?: "home") {
    "settings" -> "Settings"
    else -> "SecondMind"
  }
}

@Composable
fun HomeScreen(onSettings: () -> Unit) {
  val ctx = LocalContext.current
  val scope = rememberCoroutineScope()
  val countFlow = remember { ctx.dataStore.data.map { it[Keys.COUNT] ?: 0 } }
  val count by countFlow.collectAsState(initial = 0)

  val notifPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { }
  LaunchedEffect(Unit) {
    if (Build.VERSION.SDK_INT >= 33) notifPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
  }

  Column(
    Modifier.fillMaxSize().padding(24.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text("SecondMind Compose", fontSize = 24.sp)
    Text("Tap to leap forward → $count")
    Button(onClick = { scope.launch { ctx.dataStore.edit { it[Keys.COUNT] = count + 1 } } }) { Text("Increment") }
    OutlinedButton(onClick = onSettings) { Text("Settings") }
    OutlinedButton(onClick = { showLocalNotification(ctx) }) { Text("Test Notification") }
    OutlinedButton(onClick = { ctx.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)) }) { Text("Enable Notification Listener") }
  }
}

private fun showLocalNotification(ctx: Context) {
  val n = NotificationCompat.Builder(ctx, "sm")
    .setContentTitle("SecondMind")
    .setContentText("Hello from your app")
    .setSmallIcon(android.R.drawable.ic_dialog_info)
    .build()
  NotificationManagerCompat.from(ctx).notify(1, n)
}

@Composable
fun SettingsScreen(onBack: () -> Unit) {
  val ctx = LocalContext.current
  val scope = rememberCoroutineScope()
  val flow = remember { ctx.dataStore.data.map { it[Keys.THEME] ?: "system" } }
  val theme by flow.collectAsState(initial = "system")

  Column(
    Modifier.fillMaxSize().padding(24.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text("Settings", fontSize = 22.sp)
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      OutlinedButton(onClick = { scope.launch { ctx.dataStore.edit { it[Keys.THEME] = "system" } } }) { Text("System") }
      OutlinedButton(onClick = { scope.launch { ctx.dataStore.edit { it[Keys.THEME] = "light" } } }) { Text("Light") }
      OutlinedButton(onClick = { scope.launch { ctx.dataStore.edit { it[Keys.THEME] = "dark" } } }) { Text("Dark") }
    }
    OutlinedButton(onClick = onBack) { Text("Back") }
  }
}

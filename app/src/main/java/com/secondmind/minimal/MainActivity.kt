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
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
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
      val themeMode = rememberThemeMode()
      MaterialTheme(colorScheme = when (themeMode) {
        "light" -> lightColorScheme()
        "dark" -> darkColorScheme()
        else -> if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
      }) {
        AppNav()
      }
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
fun rememberThemeMode(): String {
  val ctx = LocalContext.current
  val flow = remember { ctx.dataStore.data.map { it[Keys.THEME] ?: "system" } }
  val mode by flow.collectAsState(initial = "system")
  return mode
}

@Composable
fun AppNav() {
  val nav = rememberNavController()
  Scaffold(
    topBar = {
      TopAppBar(title = { Text(currentTitle(nav)) })
    }
  ) { padding ->
    NavHost(nav, "home", modifier = Modifier.padding(padding)) {
      composable("home") { HomeScreen(onSettings = { nav.navigate("settings") }) }
      composable("settings") { SettingsScreen(onBack = { nav.popBackStack() }) }
    }
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
    if (Build.VERSION.SDK_INT >= 33) {
      notifPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
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

  Column(Modifier.fillMaxSize().padding(24.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally) {

    Text("Settings", fontSize = 22.sp)
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      FilterChip("System", theme == "system") { scope.launch { ctx.dataStore.edit { it[Keys.THEME] = "system" } } }
      FilterChip("Light", theme == "light") { scope.launch { ctx.dataStore.edit { it[Keys.THEME] = "light" } } }
      FilterChip("Dark", theme == "dark") { scope.launch { ctx.dataStore.edit { it[Keys.THEME] = "dark" } } }
    }
    OutlinedButton(onClick = onBack) { Text("Back") }
  }
}

@Composable
fun FilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
  AssistChip(
    onClick = onClick,
    label = { Text(label) },
    leadingIcon = if (selected) { { Icon(Icons.Default.Check, contentDescription = null) } } else null
  )
}

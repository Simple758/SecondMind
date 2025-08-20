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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.secondmind.minimal.data.Keys
import com.secondmind.minimal.data.dataStore
import com.secondmind.minimal.home.HomeCarousel
import com.secondmind.minimal.home.NotifDiagRow
import com.secondmind.minimal.tts.Reader
import com.secondmind.minimal.ui.DetailsScreen
import com.secondmind.minimal.ui.InboxScreen
import com.secondmind.minimal.ui.components.NotificationAccessBanner
import com.secondmind.minimal.ui.components.QuickNoteCard
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch










class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ensureChannel()
    setContent {
      val mode by rememberThemeMode()
      val dark = when (mode) { "dark" -> true; "light" -> false; else -> isSystemInDarkTheme() }
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
  Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text(titleFor(nav)) }) }) { pad ->
    NavHost(nav, startDestination = "home", modifier = Modifier.padding(pad)) {
      composable("home") { HomeScreen(onSettings = { nav.navigate("settings") }, onInbox = { nav.navigate("inbox") }) }
      composable("settings") { SettingsScreen(onBack = { nav.popBackStack() }) }
      composable("inbox") { InboxScreen() }
      composable(
        route = "notification/{id}",
        arguments = listOf(navArgument("id"){ type = NavType.LongType })
      ) { back ->
        val id = back.arguments?.getLong("id") ?: -1L
        DetailsScreen(id)
      }
    }
  }
}

@Composable
fun titleFor(nav: NavHostController): String {
  val e by nav.currentBackStackEntryAsState()
  return when (e?.destination?.route?.substringBefore("/")) {
    "settings" -> "Settings"
    "inbox" -> "Inbox"
    "notification" -> "Details"
    else -> "SecondMind"
  }
}

@Composable
fun HomeScreen(onSettings: () -> Unit, onInbox: () -> Unit) {
  val ctx = LocalContext.current
  val scope = rememberCoroutineScope()
  val countFlow = remember { ctx.dataStore.data.map { it[Keys.COUNT] ?: 0 } }
  val count by countFlow.collectAsState(initial = 0)
  val notifPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { }
  LaunchedEffect(Unit) { if (Build.VERSION.SDK_INT >= 33) notifPermission.launch(Manifest.permission.POST_NOTIFICATIONS) }

  Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
    HomeCarousel(modifier = Modifier.padding(top = 8.dp))
                Spacer(Modifier.height(12.dp))
                com.secondmind.minimal.feature.wiki.WikiBrainFoodCard()
                Spacer(Modifier.height(12.dp))
                com.secondmind.minimal.feature.youtube.YtWatchLaterCard()

    NotifDiagRow(modifier = Modifier.padding(bottom = 8.dp))

    NotificationAccessBanner(modifier = Modifier.padding(bottom = 12.dp))
    Text("Tap to leap forward → $count")
    Button(onClick = { scope.launch { ctx.dataStore.edit { it[Keys.COUNT] = count + 1 } } }) { Text("Increment") }
    OutlinedButton(onClick = onInbox) { Text("Inbox") }
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

  val themeFlow = remember { ctx.dataStore.data.map { it[Keys.THEME] ?: "system" } }
  val theme by themeFlow.collectAsState(initial = "system")

  val retentionFlow = remember { ctx.dataStore.data.map { it[Keys.RETENTION_DAYS] ?: 7 } }
  val retention by retentionFlow.collectAsState(initial = 7)

  val enabledFlow = remember { ctx.dataStore.data.map { it[Keys.READER_ENABLED] ?: true } }
  val readerEnabled by enabledFlow.collectAsState(initial = true)

  val rateFlow = remember { ctx.dataStore.data.map { it[Keys.READER_RATE] ?: 1.0f } }
  val readerRate by rateFlow.collectAsState(initial = 1.0f)

  val pitchFlow = remember { ctx.dataStore.data.map { it[Keys.READER_PITCH] ?: 1.0f } }
  val readerPitch by pitchFlow.collectAsState(initial = 1.0f)

  LaunchedEffect(readerEnabled, readerRate, readerPitch) {
    Reader.updateConfig(readerEnabled, readerRate, readerPitch, ctx)
  }

  Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
    Text("Settings", fontSize = 22.sp)

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      OutlinedButton(onClick = { scope.launch { ctx.dataStore.edit { it[Keys.THEME] = "system" } } }) { Text("System") }
      OutlinedButton(onClick = { scope.launch { ctx.dataStore.edit { it[Keys.THEME] = "light" } } }) { Text("Light") }
      OutlinedButton(onClick = { scope.launch { ctx.dataStore.edit { it[Keys.THEME] = "dark" } } }) { Text("Dark") }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
      Text("Retention (days): $retention")
      OutlinedButton(onClick = { scope.launch { ctx.dataStore.edit { it[Keys.RETENTION_DAYS] = maxOf(1, retention - 1) } } }) { Text("-") }
      OutlinedButton(onClick = { scope.launch { ctx.dataStore.edit { it[Keys.RETENTION_DAYS] = retention + 1 } } }) { Text("+") }
    }

    Divider()

    Text("Reader", style = MaterialTheme.typography.titleMedium)

    val idText = remember {
      android.content.ComponentName(
        ctx, com.secondmind.minimal.access.SecondMindAccessibilityService::class.java
      ).flattenToString()
    }
    val enabledNow = remember {
      val v = android.provider.Settings.Secure.getString(
        ctx.contentResolver,
        android.provider.Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
      )
      v?.contains(idText) == true
    }
    Text("Accessibility: " + if (enabledNow) "ON" else "OFF")
    Text("Service ID: " + idText, fontSize = 12.sp)
    
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      Text("Enabled")
      Switch(checked = readerEnabled, onCheckedChange = { v -> scope.launch { ctx.dataStore.edit { it[Keys.READER_ENABLED] = v } } })
    }
    Column(Modifier.fillMaxWidth().padding(horizontal = 8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
      Text("Rate: ${"%.2f".format(readerRate)}")
      Slider(value = readerRate, onValueChange = { v -> scope.launch { ctx.dataStore.edit { it[Keys.READER_RATE] = v.coerceIn(0.5f, 1.5f) } } }, valueRange = 0.5f..1.5f, steps = 10)
      Text("Pitch: ${"%.2f".format(readerPitch)}")
      Slider(value = readerPitch, onValueChange = { v -> scope.launch { ctx.dataStore.edit { it[Keys.READER_PITCH] = v.coerceIn(0.5f, 1.5f) } } }, valueRange = 0.5f..1.5f, steps = 10)
    }

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      OutlinedButton(onClick = { Reader.speak(ctx, "This is a test of the SecondMind reader.") }) { Text("Test Read") }
      OutlinedButton(onClick = {
        val i = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        ctx.startActivity(i)
      }) { Text("Open Accessibility Settings") }
    }

    
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      OutlinedButton(onClick = {
        val ctx2 = ctx
        try {
          val cn = android.content.ComponentName(ctx2, com.secondmind.minimal.access.SecondMindAccessibilityService::class.java)
          val i = android.content.Intent("android.settings.ACCESSIBILITY_DETAILS_SETTINGS")
          i.putExtra("android.provider.extra.EXTRA_ACCESSIBILITY_COMPONENT_NAME", cn.flattenToString())
          ctx2.startActivity(i)
        } catch (e: Throwable) {
          ctx2.startActivity(android.content.Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
      }) { Text("Toggle My Accessibility") }
    }

    OutlinedButton(onClick = onBack) { Text("Back") }
  }
}

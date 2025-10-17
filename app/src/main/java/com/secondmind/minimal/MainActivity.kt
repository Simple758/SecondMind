mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class, androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
package com.secondmind.minimal
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.foundation.layout.padding
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.foundation.layout.Box
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.material.icons.filled.SmartToy
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.material3.FloatingActionButton
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import com.secondmind.minimal.ui.NavigationRoutes
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.runtime.LaunchedEffect
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.ui.graphics.Color
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.runtime.getValue
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.material3.lightColorScheme
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.material3.darkColorScheme
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.material3.MaterialTheme
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.foundation.layout.imePadding
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.foundation.layout.heightIn
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.material3.IconButton
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.material3.Icon
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import android.Manifest
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import android.app.NotificationChannel
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import android.app.NotificationManager
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import android.content.Context
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import android.content.Intent
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import android.os.Build
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import android.os.Bundle
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import android.provider.Settings
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.activity.ComponentActivity
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.activity.compose.setContent
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.activity.result.contract.ActivityResultContracts
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.foundation.isSystemInDarkTheme
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.foundation.layout.*
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.foundation.layout.Arrangement
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.foundation.layout.FlowRow
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.foundation.layout.ExperimentalLayoutApi
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.foundation.rememberScrollState
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.foundation.verticalScroll
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.material3.*
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.material.icons.Icons
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.material.icons.filled.Menu
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.runtime.*
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.ui.Alignment
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.ui.Modifier
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.ui.platform.LocalContext
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.ui.unit.dp
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.ui.unit.sp
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.core.app.NotificationCompat
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.core.app.NotificationManagerCompat
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.datastore.preferences.core.edit
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.navigation.NavHostController
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.navigation.NavType
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.navigation.compose.*
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.navigation.navArgument
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import com.secondmind.minimal.data.Keys
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import com.secondmind.minimal.data.dataStore
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import com.secondmind.minimal.home.HomeCarousel
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import com.secondmind.minimal.home.NotifDiagRow
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import com.secondmind.minimal.tts.Reader
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import com.secondmind.minimal.ui.DetailsScreen
import com.secondmind.minimal.presentation.audiobook.AudiobookScreen
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import com.secondmind.minimal.ui.InboxScreen
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import com.secondmind.minimal.ui.components.NotificationAccessBanner
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import com.secondmind.minimal.ui.components.QuickNoteCard
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import kotlinx.coroutines.flow.map
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import kotlinx.coroutines.launch
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import com.secondmind.minimal.news.NewsPanel
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.foundation.lazy.LazyColumn
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.material3.ModalNavigationDrawer
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.material3.rememberDrawerState
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
import androidx.compose.material3.DrawerValue
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
class MainActivity : ComponentActivity() {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  override fun onCreate(savedInstanceState: Bundle?) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    super.onCreate(savedInstanceState)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
// ensureChannel(…) removed for CI
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    setContent {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
val mode by rememberThemeMode()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      val dark = when (mode) { "dark" -> true; "light" -> false; else -> isSystemInDarkTheme() }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
val scheme = if (dark) darkColorScheme() else lightColorScheme()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      MaterialTheme(colorScheme = scheme) { AppNav() }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
//   private fun ensureChannel() {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
//     if (Build.VERSION.SDK_INT >= 26) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
//       val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
//       nm.createNotificationChannel(NotificationChannel("sm", "SecondMind", NotificationManager.IMPORTANCE_DEFAULT))
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
//     }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
@Composable
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
fun rememberThemeMode(): State<String> {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  val ctx = LocalContext.current
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  val flow = remember { ctx.dataStore.data.map { it[Keys.THEME] ?: "system" }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
return flow.collectAsState(initial = "system")
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
@Composable
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
fun AppNav() {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  val nav = rememberNavController()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  val drawerState = rememberDrawerState(DrawerValue.Closed)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  val backstack by nav.currentBackStackEntryAsState()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  val currentRoute = backstack?.destination?.route?.substringBefore("/") ?: com.secondmind.minimal.ui.NavigationRoutes.HOME
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  ModalNavigationDrawer(
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    drawerState = drawerState,
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    drawerContent = {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      com.secondmind.minimal.ui.DrawerContent(
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
        selectedRoute = currentRoute,
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
        onDestinationClicked = { route -> nav.navigate(route) { launchSingleTop = true }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
,
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
        drawerState = drawerState
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      )
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    Scaffold(containerColor = Color.Black, topBar = { }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
) { pad ->
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      Box(Modifier.fillMaxSize().padding(pad)) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
        NavHost(nav, startDestination = "home", modifier = Modifier.fillMaxSize()) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
              composable("home") { HomeScreen(navController = nav, onSettings = { nav.navigate("settings") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
, onInbox = { nav.navigate("inbox") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
, onOpenNews = { nav.navigate("news") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
) }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
composable("settings") { com.secondmind.minimal.ui.SettingsScreen(onBack = { nav.popBackStack() }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
) }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
// InboxAIOverlay
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    composable("inbox") {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      val ctx = androidx.compose.ui.platform.LocalContext.current
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      androidx.compose.foundation.layout.Box(modifier = androidx.compose.ui.Modifier.fillMaxSize()) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
        InboxScreen()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
        androidx.compose.material3.FloatingActionButton(
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
          onClick = {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
            com.secondmind.minimal.inbox.InboxGate.active = true
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
            com.secondmind.minimal.notify.SecondMindNotificationListener.triggerRebind(ctx)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
          },
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
          modifier = androidx.compose.ui.Modifier
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
            .align(androidx.compose.ui.Alignment.BottomEnd)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
            .padding(16.dp)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
        ) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
          androidx.compose.material3.Icon(
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
            imageVector = androidx.compose.material.icons.Icons.Filled.SmartToy,
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
            contentDescription = "AI"
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
          )
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
        }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
composable("news") { com.secondmind.minimal.news.NewsPanel(modifier = Modifier.fillMaxSize()) }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  composable("wiki") { com.secondmind.minimal.feature.wiki.WikiScreen() }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  composable("ai") {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    val ctx = androidx.compose.ui.platform.LocalContext.current
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    androidx.compose.runtime.LaunchedEffect("open_ai") {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      ctx.startActivity(android.content.Intent(ctx, com.secondmind.minimal.ai.AiChatActivity::class.java))
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      nav.popBackStack()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
composable("developer") {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    val ctx = androidx.compose.ui.platform.LocalContext.current
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    androidx.compose.runtime.LaunchedEffect("open_dev") {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      ctx.startActivity(android.content.Intent(ctx, com.secondmind.minimal.dev.DeveloperActivity::class.java))
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      nav.popBackStack()

            // Audiobook route
            composable(NavigationRoutes.AUDIOBOOKS) {
                val vm = remember { AudiobookViewModel() }
                AudiobookScreen(vm = vm)
            }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
composable(
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
                route = "notification/{id}  ",
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
                arguments = listOf(navArgument("id"){ type = NavType.LongType }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
              ) { back ->
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
                val id = back.arguments?.getLong("id") ?: -1L
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
                DetailsScreen(id)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
              }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
@Composable
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
fun titleFor(nav: NavHostController): String {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  val e by nav.currentBackStackEntryAsState()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  return when (e?.destination?.route?.substringBefore("/")) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    "settings" -> "Settings"
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    "inbox" -> "Inbox"
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    "notification" -> "Details"
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    else -> "SecondMind"
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
private fun showLocalNotification(ctx: Context) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  val n = NotificationCompat.Builder(ctx, "sm")
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    .setContentTitle("SecondMind")
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    .setContentText("Hello from your app")
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    .setSmallIcon(android.R.drawable.ic_dialog_info)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    .build()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  NotificationManagerCompat.from(ctx).notify(1, n)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
@Composable
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
fun HomeScreen(navController: androidx.navigation.NavController, onSettings: () -> Unit, onInbox: () -> Unit, onOpenNews: () -> Unit) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  androidx.compose.foundation.layout.Column(
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  modifier = androidx.compose.ui.Modifier
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    .fillMaxSize()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    .padding(16.dp),
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  androidx.compose.foundation.layout.Row(
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  ) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    androidx.compose.material3.OutlinedButton(onClick = onInbox) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      androidx.compose.material3.Text("Inbox")
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
androidx.compose.material3.OutlinedButton(onClick = onSettings) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      androidx.compose.material3.Text("Settings")
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
com.secondmind.minimal.home.HomeCarousel(navController = navController, 
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    modifier = androidx.compose.ui.Modifier.fillMaxSize(),
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    onOpenNews = onOpenNews
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  )
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
@Composable
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
fun SettingsScreen(onBack: () -> Unit) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  val ctx = LocalContext.current
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  val scope = rememberCoroutineScope()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  val themeFlow = remember { ctx.dataStore.data.map { it[Keys.THEME] ?: "system" }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
val retentionFlow = remember { ctx.dataStore.data.map { it[Keys.RETENTION_DAYS] ?: 7 }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
val enabledFlow = remember { ctx.dataStore.data.map { it[Keys.READER_ENABLED] ?: true }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
val rateFlow = remember { ctx.dataStore.data.map { it[Keys.READER_RATE] ?: 1.0f }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
val pitchFlow = remember { ctx.dataStore.data.map { it[Keys.READER_PITCH] ?: 1.0f }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
val theme by themeFlow.collectAsState(initial = "system")
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  val retention by retentionFlow.collectAsState(initial = 7)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  val readerEnabled by enabledFlow.collectAsState(initial = true)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  val readerRate by rateFlow.collectAsState(initial = 1.0f)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  val readerPitch by pitchFlow.collectAsState(initial = 1.0f)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  LaunchedEffect(readerEnabled, readerRate, readerPitch) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    Reader.updateConfig(readerEnabled, readerRate, readerPitch, ctx)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
Box(
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    Modifier.fillMaxSize()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      .imePadding()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
  ) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    Column(
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      Modifier.fillMaxWidth().padding(24.dp),
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      verticalArrangement = Arrangement.spacedBy(16.dp),
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      horizontalAlignment = Alignment.CenterHorizontally
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
    ) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      Text("Settings", fontSize = 22.sp)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
        OutlinedButton(onClick = { scope.launch { ctx.dataStore.edit { it[Keys.THEME] = "system" }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
) { Text("System") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
OutlinedButton(onClick = { scope.launch { ctx.dataStore.edit { it[Keys.THEME] = "light" }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
) { Text("Light") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
OutlinedButton(onClick = { scope.launch { ctx.dataStore.edit { it[Keys.THEME] = "dark" }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
) { Text("Dark") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
        Text("Retention (days): $retention")
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
        OutlinedButton(onClick = { scope.launch { ctx.dataStore.edit { it[Keys.RETENTION_DAYS] = maxOf(1, retention - 1) }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
) { Text("-") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
OutlinedButton(onClick = { scope.launch { ctx.dataStore.edit { it[Keys.RETENTION_DAYS] = retention + 1 }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
) { Text("+") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
Divider()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      Text("Reader", style = MaterialTheme.typography.titleMedium)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      val idText = remember {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
        android.content.ComponentName(
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
          ctx, com.secondmind.minimal.access.SecondMindAccessibilityService::class.java
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
        ).flattenToString()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
val enabledNow = remember {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
        val v = android.provider.Settings.Secure.getString(
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
          ctx.contentResolver,
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
          android.provider.Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
        )
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
        v?.contains(idText) == true
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
Text("Accessibility: " + if (enabledNow) "ON" else "OFF")
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      Text("Service ID: " + idText, fontSize = 12.sp)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
        Text("Enabled")
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
        Switch(checked = readerEnabled, onCheckedChange = { v -> scope.launch { ctx.dataStore.edit { it[Keys.READER_ENABLED] = v }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
Column(Modifier.fillMaxWidth().padding(horizontal = 8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
        Text("Rate: ${"%.2f".format(readerRate)}  ")
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
        Slider(value = readerRate,
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
               onValueChange = { v -> scope.launch { ctx.dataStore.edit { it[Keys.READER_RATE] = v.coerceIn(0.5f, 1.5f) }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
,
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
               valueRange = 0.5f..1.5f, steps = 10)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
        Text("Pitch: ${"%.2f".format(readerPitch)}  ")
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
        Slider(value = readerPitch,
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
               onValueChange = { v -> scope.launch { ctx.dataStore.edit { it[Keys.READER_PITCH] = v.coerceIn(0.5f, 1.5f) }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
,
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
               valueRange = 0.5f..1.5f, steps = 10)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
      }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
        com.secondmind.minimal.ui.TtsSettings()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
        OutlinedButton(onClick = { com.secondmind.minimal.tts.Reader.stop() }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
) { Text("Stop reading") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
OutlinedButton(onClick = { Reader.speak(ctx, "This is a test of the SecondMind reader.") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
) { Text("Test Read") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
OutlinedButton(onClick = {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
          val i = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
          ctx.startActivity(i)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
        }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
) { Text("Open Accessibility Settings") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
        OutlinedButton(onClick = {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
          val ctx2 = ctx
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
          try {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
            val cn = android.content.ComponentName(ctx2, com.secondmind.minimal.access.SecondMindAccessibilityService::class.java)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
            val i = android.content.Intent("android.settings.ACCESSIBILITY_DETAILS_SETTINGS")
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
            i.putExtra("android.provider.extra.EXTRA_ACCESSIBILITY_COMPONENT_NAME", cn.flattenToString())
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
            ctx2.startActivity(i)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
          }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
catch (e: Throwable) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
            ctx2.startActivity(android.content.Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS))
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
          }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
) { Text("Toggle My Accessibility") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
OutlinedButton(onClick = onBack) { Text("Back") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
}

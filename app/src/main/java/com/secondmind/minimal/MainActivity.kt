mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class, androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
package com.secondmind.minimal
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.foundation.layout.padding
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.foundation.layout.Box
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.material.icons.filled.SmartToy
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.material3.FloatingActionButton
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import com.secondmind.minimal.ui.NavigationRoutes
import com.secondmind.minimal.presentation.audiobook.AudiobookScreen
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import com.secondmind.minimal.presentation.audiobook.AudiobookScreen
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.runtime.LaunchedEffect
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.ui.graphics.Color
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.navigation.compose.currentBackStackEntryAsState
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.runtime.getValue
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.material3.lightColorScheme
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.material3.darkColorScheme
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.material3.MaterialTheme
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.foundation.layout.imePadding
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.foundation.layout.heightIn
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.material3.IconButton
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.material3.Icon
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import android.Manifest
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import android.app.NotificationChannel
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import android.app.NotificationManager
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import android.content.Context
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import android.content.Intent
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import android.os.Build
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import android.os.Bundle
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import android.provider.Settings
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.activity.ComponentActivity
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.activity.compose.rememberLauncherForActivityResult
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.activity.compose.setContent
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.activity.result.contract.ActivityResultContracts
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.foundation.isSystemInDarkTheme
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.foundation.layout.*
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.foundation.layout.Arrangement
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.foundation.layout.FlowRow
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.foundation.layout.ExperimentalLayoutApi
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.foundation.rememberScrollState
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.foundation.verticalScroll
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.material3.*
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.material.icons.Icons
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.material.icons.filled.Menu
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.runtime.*
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.ui.Alignment
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.ui.Modifier
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.ui.platform.LocalContext
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.ui.unit.dp
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.ui.unit.sp
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.core.app.NotificationCompat
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.core.app.NotificationManagerCompat
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.datastore.preferences.core.edit
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.navigation.NavHostController
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.navigation.NavType
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.navigation.compose.*
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.navigation.navArgument
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import com.secondmind.minimal.data.Keys
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import com.secondmind.minimal.data.dataStore
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import com.secondmind.minimal.home.HomeCarousel
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import com.secondmind.minimal.home.NotifDiagRow
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import com.secondmind.minimal.tts.Reader
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import com.secondmind.minimal.ui.DetailsScreen
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import com.secondmind.minimal.ui.InboxScreen
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import com.secondmind.minimal.ui.components.NotificationAccessBanner
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import com.secondmind.minimal.ui.components.QuickNoteCard
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import kotlinx.coroutines.flow.map
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import kotlinx.coroutines.launch
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import com.secondmind.minimal.news.NewsPanel
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.foundation.lazy.LazyColumn
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.material3.ModalNavigationDrawer
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.material3.rememberDrawerState
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
import androidx.compose.material3.DrawerValue
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
class MainActivity : ComponentActivity() {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  override fun onCreate(savedInstanceState: Bundle?) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    super.onCreate(savedInstanceState)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
// ensureChannel(…) removed for CI
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    setContent {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
val mode by rememberThemeMode()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      val dark = when (mode) { "dark" -> true; "light" -> false; else -> isSystemInDarkTheme() }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
val scheme = if (dark) darkColorScheme() else lightColorScheme()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      MaterialTheme(colorScheme = scheme) { AppNav() }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
//   private fun ensureChannel() {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
//     if (Build.VERSION.SDK_INT >= 26) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
//       val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
//       nm.createNotificationChannel(NotificationChannel("sm", "SecondMind", NotificationManager.IMPORTANCE_DEFAULT))
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
//     }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
@Composable
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
fun rememberThemeMode(): State<String> {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  val ctx = LocalContext.current
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  val flow = remember { ctx.dataStore.data.map { it[Keys.THEME] ?: "system" }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
return flow.collectAsState(initial = "system")
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
@Composable
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
fun AppNav() {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  val nav = rememberNavController()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  val drawerState = rememberDrawerState(DrawerValue.Closed)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  val backstack by nav.currentBackStackEntryAsState()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  val currentRoute = backstack?.destination?.route?.substringBefore("/") ?: com.secondmind.minimal.ui.NavigationRoutes.HOME
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  ModalNavigationDrawer(
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    drawerState = drawerState,
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    drawerContent = {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      com.secondmind.minimal.ui.DrawerContent(
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
        selectedRoute = currentRoute,
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
        onDestinationClicked = { route -> nav.navigate(route) { launchSingleTop = true }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
,
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
        drawerState = drawerState
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      )
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    Scaffold(containerColor = Color.Black, topBar = { }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
) { pad ->
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      Box(Modifier.fillMaxSize().padding(pad)) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
        NavHost(nav, startDestination = "home", modifier = Modifier.fillMaxSize()) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
              composable("home") { HomeScreen(navController = nav, onSettings = { nav.navigate("settings") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
, onInbox = { nav.navigate("inbox") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
, onOpenNews = { nav.navigate("news") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
) }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
composable("settings") { com.secondmind.minimal.ui.SettingsScreen(onBack = { nav.popBackStack() }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
) }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
// InboxAIOverlay
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    composable("inbox") {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      val ctx = androidx.compose.ui.platform.LocalContext.current
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      androidx.compose.foundation.layout.Box(modifier = androidx.compose.ui.Modifier.fillMaxSize()) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
        InboxScreen()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
        androidx.compose.material3.FloatingActionButton(
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
          onClick = {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
            com.secondmind.minimal.inbox.InboxGate.active = true
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
            com.secondmind.minimal.notify.SecondMindNotificationListener.triggerRebind(ctx)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
          },
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
          modifier = androidx.compose.ui.Modifier
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
            .align(androidx.compose.ui.Alignment.BottomEnd)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
            .padding(16.dp)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
        ) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
          androidx.compose.material3.Icon(
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
            imageVector = androidx.compose.material.icons.Icons.Filled.SmartToy,
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
            contentDescription = "AI"
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
          )
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
        }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
composable("news") { com.secondmind.minimal.news.NewsPanel(modifier = Modifier.fillMaxSize()) }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  composable("wiki") { com.secondmind.minimal.feature.wiki.WikiScreen() }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
          // Audiobook feature
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
          composable(NavigationRoutes.AUDIOBOOKS) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
            val vm = remember { AudiobookViewModel() }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
            AudiobookScreen(vm = vm, onOpenPlayer = { nav.navigate("audiobook_player") })
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
          }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
          composable("audiobook_player") {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
            val vm = remember { AudiobookViewModel() }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
            val state by vm.state.collectAsState()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
            state.current?.let { AudiobookPlayerScreen(book = it) }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
          }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
          // Audiobook feature
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
          composable() {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
            val vm = remember { AudiobookViewModel() }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
            val state by vm.state.collectAsState()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
            state.current?.let { AudiobookPlayerScreen(book = it) }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
          }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  composable("ai") {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    val ctx = androidx.compose.ui.platform.LocalContext.current
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    androidx.compose.runtime.LaunchedEffect("open_ai") {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      ctx.startActivity(android.content.Intent(ctx, com.secondmind.minimal.ai.AiChatActivity::class.java))
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      nav.popBackStack()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
composable("developer") {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    val ctx = androidx.compose.ui.platform.LocalContext.current
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    androidx.compose.runtime.LaunchedEffect("open_dev") {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      ctx.startActivity(android.content.Intent(ctx, com.secondmind.minimal.dev.DeveloperActivity::class.java))
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      nav.popBackStack()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
composable(
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
                route = "notification/{id}  ",
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
                arguments = listOf(navArgument("id"){ type = NavType.LongType }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
              ) { back ->
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
                val id = back.arguments?.getLong("id") ?: -1L
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
                DetailsScreen(id)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
              }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
@Composable
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
fun titleFor(nav: NavHostController): String {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  val e by nav.currentBackStackEntryAsState()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  return when (e?.destination?.route?.substringBefore("/")) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    "settings" -> "Settings"
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    "inbox" -> "Inbox"
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    "notification" -> "Details"
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    else -> "SecondMind"
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
private fun showLocalNotification(ctx: Context) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  val n = NotificationCompat.Builder(ctx, "sm")
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    .setContentTitle("SecondMind")
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    .setContentText("Hello from your app")
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    .setSmallIcon(android.R.drawable.ic_dialog_info)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    .build()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  NotificationManagerCompat.from(ctx).notify(1, n)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
@Composable
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
fun HomeScreen(navController: androidx.navigation.NavController, onSettings: () -> Unit, onInbox: () -> Unit, onOpenNews: () -> Unit) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  androidx.compose.foundation.layout.Column(
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  modifier = androidx.compose.ui.Modifier
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    .fillMaxSize()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    .padding(16.dp),
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  androidx.compose.foundation.layout.Row(
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  ) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    androidx.compose.material3.OutlinedButton(onClick = onInbox) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      androidx.compose.material3.Text("Inbox")
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
androidx.compose.material3.OutlinedButton(onClick = onSettings) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      androidx.compose.material3.Text("Settings")
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
com.secondmind.minimal.home.HomeCarousel(navController = navController, 
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    modifier = androidx.compose.ui.Modifier.fillMaxSize(),
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    onOpenNews = onOpenNews
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  )
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
@Composable
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
fun SettingsScreen(onBack: () -> Unit) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  val ctx = LocalContext.current
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  val scope = rememberCoroutineScope()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  val themeFlow = remember { ctx.dataStore.data.map { it[Keys.THEME] ?: "system" }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
val retentionFlow = remember { ctx.dataStore.data.map { it[Keys.RETENTION_DAYS] ?: 7 }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
val enabledFlow = remember { ctx.dataStore.data.map { it[Keys.READER_ENABLED] ?: true }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
val rateFlow = remember { ctx.dataStore.data.map { it[Keys.READER_RATE] ?: 1.0f }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
val pitchFlow = remember { ctx.dataStore.data.map { it[Keys.READER_PITCH] ?: 1.0f }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
val theme by themeFlow.collectAsState(initial = "system")
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  val retention by retentionFlow.collectAsState(initial = 7)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  val readerEnabled by enabledFlow.collectAsState(initial = true)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  val readerRate by rateFlow.collectAsState(initial = 1.0f)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  val readerPitch by pitchFlow.collectAsState(initial = 1.0f)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  LaunchedEffect(readerEnabled, readerRate, readerPitch) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    Reader.updateConfig(readerEnabled, readerRate, readerPitch, ctx)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
Box(
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    Modifier.fillMaxSize()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      .imePadding()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
  ) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    Column(
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      Modifier.fillMaxWidth().padding(24.dp),
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      verticalArrangement = Arrangement.spacedBy(16.dp),
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      horizontalAlignment = Alignment.CenterHorizontally
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
    ) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      Text("Settings", fontSize = 22.sp)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
        OutlinedButton(onClick = { scope.launch { ctx.dataStore.edit { it[Keys.THEME] = "system" }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
) { Text("System") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
OutlinedButton(onClick = { scope.launch { ctx.dataStore.edit { it[Keys.THEME] = "light" }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
) { Text("Light") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
OutlinedButton(onClick = { scope.launch { ctx.dataStore.edit { it[Keys.THEME] = "dark" }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
) { Text("Dark") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
        Text("Retention (days): $retention")
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
        OutlinedButton(onClick = { scope.launch { ctx.dataStore.edit { it[Keys.RETENTION_DAYS] = maxOf(1, retention - 1) }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
) { Text("-") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
OutlinedButton(onClick = { scope.launch { ctx.dataStore.edit { it[Keys.RETENTION_DAYS] = retention + 1 }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
) { Text("+") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
Divider()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      Text("Reader", style = MaterialTheme.typography.titleMedium)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      val idText = remember {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
        android.content.ComponentName(
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
          ctx, com.secondmind.minimal.access.SecondMindAccessibilityService::class.java
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
        ).flattenToString()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
val enabledNow = remember {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
        val v = android.provider.Settings.Secure.getString(
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
          ctx.contentResolver,
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
          android.provider.Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
        )
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
        v?.contains(idText) == true
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
Text("Accessibility: " + if (enabledNow) "ON" else "OFF")
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      Text("Service ID: " + idText, fontSize = 12.sp)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
        Text("Enabled")
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
        Switch(checked = readerEnabled, onCheckedChange = { v -> scope.launch { ctx.dataStore.edit { it[Keys.READER_ENABLED] = v }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
Column(Modifier.fillMaxWidth().padding(horizontal = 8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
        Text("Rate: ${"%.2f".format(readerRate)}  ")
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
        Slider(value = readerRate,
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
               onValueChange = { v -> scope.launch { ctx.dataStore.edit { it[Keys.READER_RATE] = v.coerceIn(0.5f, 1.5f) }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
,
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
               valueRange = 0.5f..1.5f, steps = 10)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
        Text("Pitch: ${"%.2f".format(readerPitch)}  ")
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
        Slider(value = readerPitch,
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
               onValueChange = { v -> scope.launch { ctx.dataStore.edit { it[Keys.READER_PITCH] = v.coerceIn(0.5f, 1.5f) }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
,
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
               valueRange = 0.5f..1.5f, steps = 10)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
      }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
        com.secondmind.minimal.ui.TtsSettings()
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
        OutlinedButton(onClick = { com.secondmind.minimal.tts.Reader.stop() }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
) { Text("Stop reading") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
OutlinedButton(onClick = { Reader.speak(ctx, "This is a test of the SecondMind reader.") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
) { Text("Test Read") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
OutlinedButton(onClick = {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
          val i = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
          ctx.startActivity(i)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
        }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
) { Text("Open Accessibility Settings") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
        OutlinedButton(onClick = {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
          val ctx2 = ctx
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
          try {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
            val cn = android.content.ComponentName(ctx2, com.secondmind.minimal.access.SecondMindAccessibilityService::class.java)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
            val i = android.content.Intent("android.settings.ACCESSIBILITY_DETAILS_SETTINGS")
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
            i.putExtra("android.provider.extra.EXTRA_ACCESSIBILITY_COMPONENT_NAME", cn.flattenToString())
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
            ctx2.startActivity(i)
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
          }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
catch (e: Throwable) {
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
            ctx2.startActivity(android.content.Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS))
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
          }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
) { Text("Toggle My Accessibility") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
OutlinedButton(onClick = onBack) { Text("Back") }
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen

mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}
mport com.secondmind.minimal.presentation.audiobook.AudiobookViewModel
mport com.secondmind.minimal.presentation.audiobook.AudiobookPlayerScreen
}

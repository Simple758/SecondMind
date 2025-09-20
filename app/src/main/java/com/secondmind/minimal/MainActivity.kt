@file:OptIn(
    androidx.compose.material3.ExperimentalMaterial3Api::class,
    androidx.compose.foundation.layout.ExperimentalLayoutApi::class
)

package com.secondmind.minimal
import androidx.compose.runtime.getValue
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
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
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import com.secondmind.minimal.news.NewsPanel
import androidx.compose.foundation.lazy.LazyColumn
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
// ensureChannel(…) removed for CI
    setContent {
    
val mode by rememberThemeMode()
      val dark = when (mode) { "dark" -> true; "light" -> false; else -> isSystemInDarkTheme() }
      val scheme = if (dark) darkColorScheme() else lightColorScheme()
      MaterialTheme(colorScheme = scheme) { AppNav() }
  }
//   private fun ensureChannel() {
//     if (Build.VERSION.SDK_INT >= 26) {
//       val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//       nm.createNotificationChannel(NotificationChannel("sm", "SecondMind", NotificationManager.IMPORTANCE_DEFAULT))
//     }
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
  Scaffold(topBar = { TopBarWithMenu(nav) }) { pad ->

Box(Modifier.fillMaxSize().padding(pad)) {
    
NavHost(nav, startDestination = "home", modifier = Modifier.fillMaxSize()) {
      composable("home") { HomeScreen(onSettings = { nav.navigate("settings") }, onInbox = { nav.navigate("inbox") }, onOpenNews = { nav.navigate("news") }) }
      composable("settings") { SettingsScreen(onBack = { nav.popBackStack() }) }
      composable("inbox") { InboxScreen() }
      composable("news") { com.secondmind.minimal.news.NewsPanel(modifier = Modifier.fillMaxSize()) }
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
private fun showLocalNotification(ctx: Context) {
  val n = NotificationCompat.Builder(ctx, "sm")
    .setContentTitle("SecondMind")
    .setContentText("Hello from your app")
    .setSmallIcon(android.R.drawable.ic_dialog_info)
    .build()
  NotificationManagerCompat.from(ctx).notify(1, n)
}
@Composable
fun HomeScreen(onSettings: () -> Unit, onInbox: () -> Unit, onOpenNews: () -> Unit) {
  Column(Modifier.fillMaxSize()) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      OutlinedButton(onClick = onInbox) {
        Text("Inbox")
      }
      OutlinedButton(onClick = onSettings) {
        Text("Settings")
      }
    }
    HomeCarousel(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f), // this makes the grid take up the rest of the screen
      onOpenNews = onOpenNews
    )
  }
}

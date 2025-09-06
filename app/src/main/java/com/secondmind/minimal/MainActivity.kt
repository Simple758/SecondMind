@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
package com.secondmind.minimal
// --- AGENT: Temporary NewsScreen and fetch using Retrofit ---
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.secondmind.minimal.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private data class NewsSource(val id:String?, val name:String?)
private data class NewsArticle(
    val source: NewsSource?, val author:String?, val title:String?,
    val description:String?, val url:String?, val urlToImage:String?, val publishedAt:String?, val content:String?
)
private data class TopHeadlinesResponse(val status:String?, val totalResults:Int?, val articles:List<NewsArticle>?)

private interface NewsApi {
    @GET("/v2/top-headlines")
    suspend fun top(
        @Query("category") category:String = "technology",
        @Query("country") country:String = "us",
        @Query("apiKey") apiKey:String
    ): TopHeadlinesResponse
}

@Composable
fun NewsScreen() {
    val apiKey = BuildConfig.NEWS_API_KEY
    var state by remember { mutableStateOf<Result<List<NewsArticle>>?>(null) }

    LaunchedEffect(apiKey) {
        state = try {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://newsapi.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val svc = retrofit.create(NewsApi::class.java)
            val res = withContext(Dispatchers.IO) { svc.top(apiKey = apiKey) }
            if (res.status == "ok" && !res.articles.isNullOrEmpty()) Result.success(res.articles!!) else Result.failure(Exception("No articles"))
        } catch (t:Throwable) { Result.failure(t) }
    }

    val r = state
    when {
        r == null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        r.isSuccess -> NewsList(r.getOrNull().orEmpty())
        else -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Failed to load news")
                Spacer(Modifier.height(8.dp))
                Button({ state = null }) { Text("Retry") }
            }
        }
    }
}

@Composable private fun NewsList(items: List<NewsArticle>) {
    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        items(items) { a ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    if (a.urlToImage != null) {
                        AsyncImage(model=a.urlToImage, contentDescription=null, modifier=Modifier.fillMaxWidth().height(180.dp))
                        Spacer(Modifier.height(8.dp))
                    }
                    Text(a.title ?: "(no title)", style=MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(4.dp))
                    Text(a.source?.name ?: "", style=MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        val url=a.url
                        Button(enabled = url!=null, onClick = { url?.let { it1 ->
                            val ctx = androidx.compose.ui.platform.LocalContext.current
                            ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it1)))
                        } }) { Text("Open") }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}
// --- AGENT END ---

// TODO(agent): DeepSeek wiring test

import androidx.compose.foundation.layout.imePadding
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
  Scaffold(topBar = { TopBarWithMenu(nav) }) { pad ->
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
  androidx.compose.foundation.layout.Column(modifier = androidx.compose.ui.Modifier.fillMaxSize()) {
    com.secondmind.minimal.home.HomeCarousel(modifier = androidx.compose.ui.Modifier.weight(1f))
    androidx.compose.foundation.layout.Row(
      modifier = androidx.compose.ui.Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
    ) {
      androidx.compose.material3.OutlinedButton(onClick = onInbox) { androidx.compose.material3.Text("Inbox") }
      androidx.compose.material3.OutlinedButton(onClick = onSettings) { androidx.compose.material3.Text("Settings") }
    }
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
  val retentionFlow = remember { ctx.dataStore.data.map { it[Keys.RETENTION_DAYS] ?: 7 } }
  val enabledFlow = remember { ctx.dataStore.data.map { it[Keys.READER_ENABLED] ?: true } }
  val rateFlow = remember { ctx.dataStore.data.map { it[Keys.READER_RATE] ?: 1.0f } }
  val pitchFlow = remember { ctx.dataStore.data.map { it[Keys.READER_PITCH] ?: 1.0f } }

  val theme by themeFlow.collectAsState(initial = "system")
  val retention by retentionFlow.collectAsState(initial = 7)
  val readerEnabled by enabledFlow.collectAsState(initial = true)
  val readerRate by rateFlow.collectAsState(initial = 1.0f)
  val readerPitch by pitchFlow.collectAsState(initial = 1.0f)

  LaunchedEffect(readerEnabled, readerRate, readerPitch) {
    Reader.updateConfig(readerEnabled, readerRate, readerPitch, ctx)
  }

  Box(
    Modifier.fillMaxSize()
      .verticalScroll(rememberScrollState())
      .imePadding()
  ) {
    Column(
      Modifier.fillMaxWidth().padding(24.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
        Slider(value = readerRate,
               onValueChange = { v -> scope.launch { ctx.dataStore.edit { it[Keys.READER_RATE] = v.coerceIn(0.5f, 1.5f) } } },
               valueRange = 0.5f..1.5f, steps = 10)
        Text("Pitch: ${"%.2f".format(readerPitch)}")
        Slider(value = readerPitch,
               onValueChange = { v -> scope.launch { ctx.dataStore.edit { it[Keys.READER_PITCH] = v.coerceIn(0.5f, 1.5f) } } },
               valueRange = 0.5f..1.5f, steps = 10)
      }

      Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        com.secondmind.minimal.ui.TtsSettings()
        OutlinedButton(onClick = { com.secondmind.minimal.tts.Reader.stop() }) { Text("Stop reading") }
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
}



@Composable
fun TopBarWithMenu(nav: NavHostController) {
  var open by remember { mutableStateOf(false) }
  CenterAlignedTopAppBar(
    title = { Text(titleFor(nav)) },
    navigationIcon = {
      Box {
        IconButton(onClick = { open = true }) {
          Icon(Icons.Filled.Menu, contentDescription = "Menu")
        }
        DropdownMenu(expanded = open, onDismissRequest = { open = false }) {
          DropdownMenuItem(text = { Text("Home") }, onClick = {
            open = false
            nav.navigate("home") { launchSingleTop = true }
          })
          DropdownMenuItem(text = { Text("Inbox") }, onClick = {
            open = false
            nav.navigate("inbox")
          })
          DropdownMenuItem(text = { Text("Settings") }, onClick = {
            open = false
            nav.navigate("settings")
          })
        }
      }
    }
  )
}

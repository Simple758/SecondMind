package com.secondmind.minimal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.secondmind.minimal.ui.HomeTopCards
import com.secondmind.minimal.ui.QuickNoteCard
import com.secondmind.minimal.ui.InboxScreen

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent { SecondMindApp() }
  }
}

@Composable
fun SecondMindApp() {
  val nav = rememberNavController()
  MaterialTheme {
    Scaffold(
      topBar = {
        SmallTopAppBar(title = { Text("SecondMind") })
      }
    ) { inner ->
      NavHost(
        navController = nav,
        startDestination = "home",
        modifier = Modifier.padding(inner)
      ) {
        composable("home") { HomeScreen(nav) }
        composable("inbox") { InboxScreen(onBack = { nav.popBackStack() }) }
        composable("settings") { SettingsScreen(onBack = { nav.popBackStack() }) }
      }
    }
  }
}

@Composable
fun HomeScreen(navController: androidx.navigation.NavController) {
  Column(
    modifier = Modifier.fillMaxSize().padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Text("Welcome", style = MaterialTheme.typography.titleLarge)
    HomeTopCards(nav = navController) {
      QuickNoteCard(modifier = Modifier.fillMaxWidth())
    }
    OutlinedButton(onClick = { navController.navigate("settings") }) { Text("Settings") }
  }
}

@Composable
fun SettingsScreen(onBack: () -> Unit) {
  Column(
    modifier = Modifier.fillMaxSize().padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Text("Settings", style = MaterialTheme.typography.titleLarge)
    OutlinedButton(onClick = onBack) { Text("Back") }
  }
}

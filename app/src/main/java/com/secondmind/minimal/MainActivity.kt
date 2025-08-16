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
import com.secondmind.minimal.ui.InboxScreen
import com.secondmind.minimal.ui.QuickNoteCard
import com.secondmind.minimal.ui.DebugScreen

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent { App() }
  }
}

@Composable
fun App() {
  MaterialTheme {
    val nav = rememberNavController()
    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("SecondMind") }) }) { p ->
      NavHost(navController = nav, startDestination = "home", modifier = Modifier.padding(p)) {

        composable("home") {
          Column(
            Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Text("Welcome", style = MaterialTheme.typography.titleLarge)
            HomeTopCards(
              quickNote = { QuickNoteCard(modifier = Modifier.fillMaxWidth()) },
              onOpenInbox = { nav.navigate("inbox") }
            )
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = { nav.navigate("debug") }) { Text("A11y Debug") }
            OutlinedButton(onClick = { nav.navigate("settings") }) { Text("Settings") }
          }
        }

        composable("inbox")   { InboxScreen(nav) }
        composable("debug")   { DebugScreen(onBack = { nav.popBackStack() }) }
        composable("settings"){
          Column(Modifier.fillMaxSize().padding(16.dp)) {
            Text("Settings", style = MaterialTheme.typography.titleLarge)
            OutlinedButton(onClick = { nav.popBackStack() }) { Text("Back") }
          }
        }
      }
    }
  }
}

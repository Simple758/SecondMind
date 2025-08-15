package com.secondmind.minimal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MaterialTheme { Surface(Modifier.fillMaxSize()) { HomeScreen() } }
    }
  }
}

@Composable
fun HomeScreen() {
  var count by remember { mutableStateOf(0) }
  Column(
    modifier = Modifier.fillMaxSize().padding(24.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text("SecondMind Compose", fontSize = 24.sp)
    Text("Tap to leap forward → $count")
    Button(onClick = { count++ }) { Text("Increment") }
  }
}

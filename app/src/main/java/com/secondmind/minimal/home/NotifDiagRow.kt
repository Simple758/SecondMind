package com.secondmind.minimal.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.secondmind.minimal.diag.NotifDiag
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NotifDiagRow(modifier: Modifier = Modifier) {
  val ctx = LocalContext.current
  var knob by remember { mutableStateOf(0) } // simple refresh

  val connectedAt = NotifDiag.connectedAt(ctx)
  val posted = NotifDiag.postedCount(ctx)
  val status = if (connectedAt > 0L) {
    val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(connectedAt))
    "Listener: connected at $time, seen $posted"
  } else {
    "Listener: not connected yet, seen $posted"
  }

  Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
    Text(status, style = MaterialTheme.typography.bodyMedium)
    TextButton(onClick = { knob++ }) { Text("Refresh") }
  }
}

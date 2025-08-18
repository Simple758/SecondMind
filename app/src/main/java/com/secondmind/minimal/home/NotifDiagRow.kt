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
  var refresh by remember { mutableStateOf(0) }

  // snapshot values on each refresh to avoid heavy observation machinery
  val connectedAt = remember(refresh) { NotifDiag.connectedAt(ctx) }
  val posted = remember(refresh) { NotifDiag.postedCount(ctx) }

  val status = if (connectedAt > 0L) {
    val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(connectedAt))
    "Listener connected @ $time • seen $posted"
  } else {
    "Listener NOT connected • seen $posted"
  }

  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(status, style = MaterialTheme.typography.bodyMedium)
    TextButton(onClick = { refresh++ }) { Text("Refresh") }
  }
}

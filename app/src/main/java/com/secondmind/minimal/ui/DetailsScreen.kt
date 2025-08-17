package com.secondmind.minimal.ui
import com.secondmind.minimal.util.putStringExtraSafe

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.secondmind.minimal.inbox.InboxViewModel
import com.secondmind.minimal.tts.Reader

@Composable
fun DetailsScreen(id: Long, vm: InboxViewModel = viewModel(factory = com.secondmind.minimal.inbox.InboxViewModel.factory(LocalContext.current.applicationContext as android.app.Application))) {
  val items by vm.notifications.collectAsState()
  val n = items.firstOrNull { it.id == id }
  val ctx = LocalContext.current

  if (n == null) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Not found") }
    return
  }

  val readText = listOfNotNull(n.title, n.text).joinToString("\n").ifBlank { "No content" }

  Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Text(n.title ?: "(no title)", style = MaterialTheme.typography.titleLarge)
    Text(n.text ?: "(no text)")
    Text("App: ${n.appPackage}")
    Text("When: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date(n.postedAt))}")
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
      Button(onClick = { Reader.speak(ctx, readText) }) { Text("Read") }
      Button(onClick = {
        val s: String? = (listOfNotNull(n.title, n.text).joinToString("\n")
        val i = Intent(Intent.ACTION_SEND).setType("text/plain").putStringExtraSafe(Intent.EXTRA_TEXT, s)
        ctx.startActivity(Intent.createChooser(i, "Share"))
      }) { Text("Share") }
      OutlinedButton(onClick = { vm.delete(n.id) }) { Text("Delete") }
    }
  }
}

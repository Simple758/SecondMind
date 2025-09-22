package com.secondmind.minimal.feature.wiki
import androidx.compose.ui.graphics.Color

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import com.secondmind.minimal.tts.Reader

@Composable
fun WikiBrainFoodCard(modifier: Modifier = Modifier) {
  val ctx = LocalContext.current
  val scope = rememberCoroutineScope()

  var summary by remember { mutableStateOf<WikiRepo.Summary?>(null) }
  var loading by remember { mutableStateOf(false) }
  var tried by remember { mutableStateOf(false) }
  var speaking by remember { mutableStateOf(false) }

  suspend fun fetch() {
    loading = true
    tried = true
    summary = withContext(Dispatchers.IO) { WikiRepo.randomSummary() }
    loading = false
  }

  LaunchedEffect(Unit) { fetch() }

  Card(
    modifier = modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = Color.Black)
  ) {
    Column(Modifier.fillMaxWidth().padding(16.dp)) {
      Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text("Brain Food (Wikipedia)", style = MaterialTheme.typography.titleLarge)
        TextButton(onClick = { scope.launch { fetch() } }) { Text("Refresh") }
      }
      Spacer(Modifier.height(8.dp))

      if (summary == null) {
        val msg = when {
          loading -> "Loading..."
          tried -> "Couldn’t load. Check internet and tap Refresh."
          else -> "Loading..."
        }
        Text(msg, style = MaterialTheme.typography.bodyMedium)
        return@Column
      }

      val s = summary!!

      Card(colors = CardDefaults.cardColors(containerColor = Color.Black)) {
        Column(Modifier.fillMaxWidth().padding(12.dp)) {
          if (!s.thumb.isNullOrBlank()) {
            NetImage(url = s.thumb!!, modifier = Modifier.fillMaxWidth().height(140.dp))
            Spacer(Modifier.height(8.dp))
          }
          Text(s.title, style = MaterialTheme.typography.titleMedium)
          Spacer(Modifier.height(6.dp))
          Text(s.extract, style = MaterialTheme.typography.bodyMedium)

          Spacer(Modifier.height(12.dp))
          Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = {
              val i = Intent(Intent.ACTION_VIEW, Uri.parse(s.url))
              runCatching { ctx.startActivity(i) }
            }) { Text("Open") }

            Spacer(Modifier.weight(1f))

            IconButton(onClick = {
              val txt = s.title + ". " + s.extract
              if (speaking) {
                Reader.stop(); speaking = false
              } else {
                Reader.speak(ctx, txt); speaking = true
              }
            }) {
              Icon(painterResource(id = android.R.drawable.ic_lock_silent_mode_off), contentDescription = "Speak")
            }
          }
        }
      }
    }
  }
}

@Composable
private fun NetImage(url: String, modifier: Modifier = Modifier) {
  var bmp by remember(url) { mutableStateOf<Bitmap?>(null) }
  LaunchedEffect(url) {
    bmp = withContext(Dispatchers.IO) {
      runCatching { URL(url).openStream().use(BitmapFactory::decodeStream) }.getOrNull()
    }
  }
  if (bmp != null) {
    Image(bmp!!.asImageBitmap(), contentDescription = null, modifier = modifier)
  }
}

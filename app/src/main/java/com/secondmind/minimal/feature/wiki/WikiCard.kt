package com.secondmind.minimal.feature.wiki
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.secondmind.minimal.feature.storage.JsonPrefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch



@Composable
private fun NetImage(url: String?, modifier: Modifier) {
    val bmp = remember(url) { mutableStateOf<android.graphics.Bitmap?>(null) }
    LaunchedEffect(url) {
        bmp.value = null
        if (url.isNullOrBlank()) return@LaunchedEffect
        bmp.value = withContext(Dispatchers.IO) {
            runCatching {
                val c = (URL(url).openConnection() as HttpURLConnection).apply {
                    connectTimeout = 5000; readTimeout = 5000
                }
                c.inputStream.use { BitmapFactory.decodeStream(it) }
            }.getOrNull()
        }
    }
    if (bmp.value != null) {
        Image(bmp.value!!.asImageBitmap(), null, modifier, contentScale = ContentScale.Crop)
    } else {
        Box(modifier.background(MaterialTheme.colorScheme.surfaceVariant))
    }
}

@Composable
fun WikiBrainFoodCard(modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val dayKey = remember {
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.US)
        "wiki.daily." + sdf.format(Date())
    }
    var tried by remember { mutableStateOf(false) }
    var summary by remember {
        val cached = JsonPrefs.getObject(ctx, dayKey)?.let {
            WikiRepo.Summary(
                it.optString("title"), it.optString("extract"),
                it.optString("url"), it.optString("thumb")
            )
        }
        mutableStateOf(cached)
    }

    suspend fun fetchAndCache() {
    suspend fun __doFetch() {
        tried = true
        val s = WikiRepo.randomSummary()
        if (s != null) {
            summary = s
            val o = org.json.JSONObject()
                .put("title", s.title).put("extract", s.extract)
                .put("url", s.url).put("thumb", s.thumb ?: "")
            JsonPrefs.put(ctx, dayKey, o)
        }
    }
    suspend fun fetchAndCache() { __doFetch() }
        val s = WikiRepo.randomSummary()
        if (s != null) {
            summary = s
            val o = org.json.JSONObject()
                .put("title", s.title).put("extract", s.extract)
                .put("url", s.url).put("thumb", s.thumb ?: "")
            JsonPrefs.put(ctx, dayKey, o)
        }
    }

    LaunchedEffect(Unit) { if (summary == null) fetchAndCache() }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Brain Food (Wikipedia)", style = MaterialTheme.typography.titleLarge)
                TextButton(onClick = { scope.launch { fetchAndCache() } }) { Text("Refresh") }

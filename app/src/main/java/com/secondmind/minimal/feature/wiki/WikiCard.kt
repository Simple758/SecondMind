package com.secondmind.minimal.feature.wiki
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.ui.res.painterResource

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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

@Composable
private fun NetImage(url: String?, modifier: Modifier) {
    val bmp = remember(url) { mutableStateOf<android.graphics.Bitmap?>(null) }
    LaunchedEffect(url) {
        bmp.value = null
        if (url.isNullOrBlank()) return@LaunchedEffect
        bmp.value = withContext(Dispatchers.IO) {
            runCatching {
                val c = (URL(url).openConnection() as HttpURLConnection).apply {
                    connectTimeout = 6000; readTimeout = 6000
                    setRequestProperty("User-Agent", "SecondMind/1.0 (+wiki)")
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

    var summary by remember {
        val cached = JsonPrefs.getObject(ctx, dayKey)?.let {
            WikiRepo.Summary(
                it.optString("title"), it.optString("extract"),
                it.optString("url"), it.optString("thumb").takeIf { s -> s.isNotBlank() }
            )
        }
        mutableStateOf(cached)
    }
    var loading by remember { mutableStateOf(false) }
    var tried by remember { mutableStateOf(false) }
    // --- TTS: safe, remembered engine ---
    val tts = remember { TextToSpeech(ctx) { /* status ignored */ } }
    var speaking by remember { mutableStateOf(false) }

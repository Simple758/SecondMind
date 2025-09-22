package com.secondmind.minimal.feature.youtube
import androidx.compose.ui.graphics.Color

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

@Composable
private fun NetImage(url: String?, modifier: Modifier, contentDesc: String?) {
    val bmp = remember(url) { mutableStateOf<android.graphics.Bitmap?>(null) }
    LaunchedEffect(url) {
        bmp.value = null
        if (url.isNullOrBlank()) return@LaunchedEffect
        bmp.value = withContext(Dispatchers.IO) {
            runCatching {
                val c = (URL(url).openConnection() as HttpURLConnection).apply {
                    connectTimeout = 6000; readTimeout = 6000
                    setRequestProperty("User-Agent", "SecondMind/1.0 (+img)")
                }
                c.inputStream.use { BitmapFactory.decodeStream(it) }
            }.getOrNull()
        }
    }
    if (bmp.value != null) {
        Image(
            bitmap = bmp.value!!.asImageBitmap(),
            contentDescription = contentDesc,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    } else {
        Box(modifier.background(Color.Black))
    }
}

@Composable
private fun rememberYtMeta(id: String): State<YtMeta?> {
    val ctx = LocalContext.current
    val state = remember(id) { mutableStateOf(YtStore.meta(ctx, id)) }
    LaunchedEffect(id) {
        if (state.value == null) {
            val url = YtId.canonicalUrl(id)
            val o = YtRepo.fetchOEmbed(url)
            val meta = if (o != null) {
                YtMeta(id, o.title, o.author, o.thumbnail)
            } else {
                YtMeta(id, "YouTube video", "", "https://img.youtube.com/vi/$id/hqdefault.jpg")
            }
            YtStore.putMeta(ctx, meta)
            state.value = meta
        }
    }
    return state
}

@Composable
fun YtWatchLaterCard(modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    var itemsState by remember { mutableStateOf(YtStore.list(ctx)) }
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
                Text("Watch Later (YouTube)", style = MaterialTheme.typography.titleLarge)
                TextButton(onClick = {
                    val id = YtStore.addFromClipboard(ctx)
                    if (id != null) itemsState = YtStore.list(ctx)
                }) { Text("Add from clipboard") }
            }
            if (itemsState.isEmpty()) {
                Text("Copy a YouTube link and tap Add from clipboard.", style = MaterialTheme.typography.bodyMedium)
                return@Column
            }
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(itemsState) { it ->
                    val meta by rememberYtMeta(it.id)
                    Card(
                        modifier = Modifier.width(260.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Black),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column {
                            NetImage(meta?.thumbUrl, Modifier.fillMaxWidth().height(140.dp), meta?.title)
                            Column(Modifier.padding(12.dp)) {
                                Text(
                                    meta?.title ?: "Loading…",
                                    maxLines = 2, overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    meta?.channel ?: "",
                                    maxLines = 1, overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Spacer(Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    TextButton(onClick = {
                                        val i = Intent(Intent.ACTION_VIEW, Uri.parse(it.url)).apply {
                                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        }
                                        runCatching { ctx.startActivity(i) }
                                    }) { Text("Open") }
                                    TextButton(onClick = {
                                        YtStore.remove(ctx, it.id)
                                        itemsState = YtStore.list(ctx)
                                    }) { Text("Remove") }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

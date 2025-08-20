package com.secondmind.minimal.feature.youtube

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

data class YtItem(val id: String, val url: String)
data class YtMeta(val id: String, val title: String, val channel: String, val thumbUrl: String)

object YtId {
    private val patterns = listOf(
        Regex("""youtu\.be/([A-Za-z0-9_-]{6,})"""),
        Regex("""youtube\.com/watch\?[^#]*v=([A-Za-z0-9_-]{6,})"""),
        Regex("""youtube\.com/shorts/([A-Za-z0-9_-]{6,})""")
    )
    fun extract(from: String): String? {
        for (r in patterns) r.find(from)?.let { return it.groupValues[1] }
        return if (from.matches(Regex("""^[A-Za-z0-9_-]{6,}$"""))) from else null
    }
    fun canonicalUrl(id: String) = "https://www.youtube.com/watch?v=$id"
}

object YtRepo {
    data class OEmbed(val title: String, val author: String, val thumbnail: String)
    suspend fun fetchOEmbed(videoUrl: String, timeoutMs: Int = 6000): OEmbed? =
        withContext(Dispatchers.IO) {
            runCatching {
                val endpoint = "https://www.youtube.com/oembed?format=json&url=$videoUrl"
                val conn = (URL(endpoint).openConnection() as HttpURLConnection).apply {
                    connectTimeout = timeoutMs; readTimeout = timeoutMs; requestMethod = "GET"
                    setRequestProperty("User-Agent", "SecondMind/1.0 (+oembed)")
                }
                conn.inputStream.use { ins ->
                    val txt = ins.readBytes().toString(Charsets.UTF_8)
                    val j = org.json.JSONObject(txt)
                    val title = j.optString("title")
                    val author = j.optString("author_name")
                    val thumb = j.optString("thumbnail_url")
                    if (title.isNotBlank() && thumb.isNotBlank()) OEmbed(title, author, thumb) else null
                }
            }.getOrNull()
        }
}

object YtStore {
    private const val KEY_LIST = "yt.list"
    private const val KEY_META_PREFIX = "yt.meta."

    private fun listRaw(ctx: android.content.Context): org.json.JSONArray =
        com.secondmind.minimal.feature.storage.JsonPrefs.getArray(ctx, KEY_LIST) ?: org.json.JSONArray()

    fun list(ctx: android.content.Context): List<YtItem> =
        (0 until listRaw(ctx).length()).mapNotNull {
            val id = listRaw(ctx).optString(it, null) ?: return@mapNotNull null
            YtItem(id, YtId.canonicalUrl(id))
        }

    private fun saveList(ctx: android.content.Context, ids: List<String>) {
        val arr = org.json.JSONArray().apply { ids.forEach { put(it) } }
        com.secondmind.minimal.feature.storage.JsonPrefs.put(ctx, KEY_LIST, arr)
    }

    fun addId(ctx: android.content.Context, id: String): Boolean {
        val ids = list(ctx).map { it.id }.toMutableList()
        if (ids.contains(id)) return false
        ids.add(0, id)
        if (ids.size > 50) ids.removeLast()
        saveList(ctx, ids)
        return true
    }

    fun remove(ctx: android.content.Context, id: String) {
        saveList(ctx, list(ctx).map { it.id }.filterNot { it == id })
        com.secondmind.minimal.feature.storage.JsonPrefs.remove(ctx, KEY_META_PREFIX + id)
    }

    fun meta(ctx: android.content.Context, id: String): YtMeta? {
        val o = com.secondmind.minimal.feature.storage.JsonPrefs.getObject(ctx, KEY_META_PREFIX + id) ?: return null
        val title = o.optString("title"); val author = o.optString("author"); val thumb = o.optString("thumb")
        if (title.isBlank() || thumb.isBlank()) return null
        return YtMeta(id, title, author, thumb)
    }
    fun putMeta(ctx: android.content.Context, m: YtMeta) {
        com.secondmind.minimal.feature.storage.JsonPrefs.put(
            ctx, KEY_META_PREFIX + m.id,
            org.json.JSONObject().put("title", m.title).put("author", m.channel).put("thumb", m.thumbUrl)
        )
    }

    fun addFromClipboard(ctx: android.content.Context): String? {
        val cm = ctx.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val text = cm.primaryClip?.takeIf { it.itemCount > 0 }?.getItemAt(0)?.text?.toString() ?: return null
        val id = YtId.extract(text) ?: return null
        addId(ctx, id)
        return id
    }
}

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
        Box(modifier.background(MaterialTheme.colorScheme.surfaceVariant))
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
                // Fallback: static thumbnail + generic title
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Row(Modifier.fillMaxWidth(),
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
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column {
                            NetImage(meta?.thumbUrl, Modifier.fillMaxWidth().height(140.dp), meta?.title)
                            Column(Modifier.padding(12.dp)) {
                                Text(meta?.title ?: "Loading…",
                                    maxLines = 2, overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.titleMedium)
                                Text(meta?.channel ?: "",
                                    maxLines = 1, overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.bodySmall)
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

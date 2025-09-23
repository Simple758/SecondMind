package com.secondmind.minimal.news
import androidx.compose.ui.graphics.Color

import android.content.Intent
import android.net.Uri
import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.secondmind.minimal.tts.Reader
import com.secondmind.minimal.ui.SafeImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@Composable
fun NewsPanel(modifier: Modifier = Modifier, initialTab: Int = 1) {
    val ctx = LocalContext.current
    LaunchedEffect(Unit){ com.secondmind.minimal.memory.MemoryStore.recordPanelOpen(ctx,"News") }

    val tabs = listOf("For you", "Tech", "Markets", "World", "Sports", "Crypto")
    var tab by remember { mutableStateOf(initialTab) }
    var isLoading by remember { mutableStateOf(false) }
    var articles by remember { mutableStateOf<List<NewsItem>>(emptyList()) }

    // Load once per tab; the API here takes no named params — we filter client-side.
    LaunchedEffect(tab) {
        isLoading = true
        try {
            val res = withContext(Dispatchers.IO) {
                RssNewsApi.fetchTopHeadlines()
            }
            articles = filterForTab(res, tab)
        } catch (_: Throwable) {
            articles = emptyList()
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Top news", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.weight(1f))
            ElevatedButton(
                onClick = { speakAllNews(ctx, articles) },
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(Icons.Filled.PlayArrow, contentDescription = "Read all")
                Spacer(Modifier.width(6.dp))
                Text("Play all")
            }
        }

        Spacer(Modifier.height(8.dp))

        ScrollableTabRow(selectedTabIndex = tab, edgePadding = 0.dp) {
            tabs.forEachIndexed { i, t ->
                Tab(selected = i == tab, onClick = { tab = i }, text = { Text(t) })
            }
        }

        Spacer(Modifier.height(12.dp))

        if (isLoading) {
            Box(Modifier.fillMaxWidth().height(220.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Column
        }

        if (articles.isNotEmpty()) {
            NewsHeroCard(
                articles.first(),
                onOpen = { url ->
                    com.secondmind.minimal.memory.MemoryStore.recordNewsOpen(ctx, articles.first().title)
                    url?.let { ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it))) }
                },
                onRefresh = { tab = tab }
            )
            Spacer(Modifier.height(12.dp))
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth().heightIn(min = 0.dp, max = 480.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 4.dp)
        ) {
            val rest = if (articles.size > 1) articles.drop(1) else emptyList()
            itemsIndexed(rest) { _, a ->
                NewsCompactCard(a) { url ->
                    com.secondmind.minimal.memory.MemoryStore.recordNewsOpen(ctx, a.title)
                    url?.let { ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it))) }
                }
            }
        }
    }
}

private fun filterForTab(items: List<NewsItem>, tab: Int): List<NewsItem> {
    // crude keyword filters so each tab looks different even if API lacks category args
    fun List<NewsItem>.byAny(words: List<String>) =
        filter { it.title?.let { t -> words.any { w -> t.contains(w, ignoreCase = true) } } == true }

    return when (tab) {
        1 -> items.byAny(listOf("AI","chip","semiconductor","software","tech","android","apple","microsoft","google","meta"))
        2 -> items.byAny(listOf("stocks","market","fed","earnings","revenue","guidance","dow","nasdaq","s&p","bond"))
        3 -> items.byAny(listOf("world","europe","asia","middle east","africa","latin","ukraine","china","india"))
        4 -> items.byAny(listOf("match","team","league","cup","goal","nba","nfl","mlb","premier","olympic","tennis","fifa"))
        5 -> items.byAny(listOf("crypto","bitcoin","btc","ethereum","eth","web3","blockchain","token","defi","nft"))
        else -> items
    }.ifEmpty { items } // fallback so the tab never looks empty
}

@Composable
private fun NewsHeroCard(article: NewsItem, onOpen: (String?) -> Unit, onRefresh: () -> Unit) {
    val ctx = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(12.dp)) {
            val img = article.bestImageUrl()
            if (!img.isNullOrBlank()) {
                SafeImage(
                    model = img,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().aspectRatio(16f/9f).clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(10.dp))
            }
            Text(article.title ?: "(no title)", style = MaterialTheme.typography.titleMedium,
                 maxLines = 3, overflow = TextOverflow.Ellipsis)
            IconButton(onClick = { Reader.speak(ctx, article.title ?: "") }) {
                Icon(Icons.Filled.PlayArrow, contentDescription = "Read")
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = { onOpen(article.url) }) { Text("Open") }
                OutlinedButton(onClick = onRefresh) { Text("Refresh") }
            }
            Spacer(Modifier.height(4.dp))
            MetaRow(article.source, null)
        }
    }
}

@Composable
private fun NewsCompactCard(article: NewsItem, onOpen: (String?) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onOpen(article.url) },
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(article.title ?: "(no title)", style = MaterialTheme.typography.bodyLarge,
                     maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(4.dp))
                MetaRow(article.source, null)
            }
            Spacer(Modifier.width(12.dp))
            Box(Modifier.size(72.dp).clip(RoundedCornerShape(12.dp))) {
                val img = article.bestImageUrl()
                if (!img.isNullOrBlank()) {
                    SafeImage(model = img, contentDescription = null,
                        contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                } else {
                    Box(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)))
                }
            }
        }
    }
}

/* ---------- helpers & small UI bits ---------- */

private fun String.forceHttpsOrNull(): String? =
    if (isBlank()) null else if (startsWith("http://")) "https://" + substring(7) else this

private fun NewsItem.bestImageUrl(): String? =
    (this.imageUrl ?: this.url).forceHttpsOrNull()

@Composable
private fun MetaRow(source: String?, publishedAt: String?) {
    val s = source?.takeIf { it.isNotBlank() }
    val t = relativeTimeOrNull(publishedAt)
    val text = listOfNotNull(s, t).joinToString(" • ")
    if (text.isNotBlank()) {
        Text(
            text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
    }
}

private fun relativeTimeOrNull(iso: String?): String? {
    if (iso.isNullOrBlank()) return null
    val patterns = arrayOf(
        "yyyy-MM-ddTHH:mm:ssZ",
        "yyyy-MM-ddTHH:mm:ss.SSSZ",
        "yyyy-MM-ddTHH:mm:ssXXX",
        "yyyy-MM-ddTHH:mm:ss.SSSXXX"
    )
    for (p in patterns) {
        try {
            val sdf = SimpleDateFormat(p, Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
                isLenient = true
            }
            val ms = sdf.parse(iso)?.time ?: continue
            return DateUtils.getRelativeTimeSpanString(
                ms, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS
            ).toString()
        } catch (_: Exception) { }
    }
    return null
}

private fun speakAllNews(ctx: android.content.Context, items: List<NewsItem>) {
    if (items.isEmpty()) return
    val script = items.joinToString(separator = ". ") { it.title ?: "" }
    Reader.speak(ctx, script)
}

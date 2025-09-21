package com.secondmind.minimal.news
import java.util.TimeZone
import java.util.Locale
import java.text.SimpleDateFormat
import android.text.format.DateUtils
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.zIndex

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.secondmind.minimal.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import com.secondmind.minimal.tts.Reader

@Composable
fun NewsPanel(modifier: Modifier = Modifier, initialTab: Int = 1) {
    val tabs = listOf("For you", "Tech", "Markets", "World", "Sports", "Crypto")
    var tab by remember { mutableStateOf(initialTab) }
    var isLoading by remember { mutableStateOf(false) }
    var articles by remember { 
    mutableStateOf<List<NewsItem>>(emptyList())
   }
    val ctx = LocalContext.current

    fun tabToCategory(i: Int): String? = when (i) {
        1 -> "technology"
        2 -> "business"
        3 -> "general"
        4 -> "sports"
        5 -> "science"
        else -> "general"
    }

    LaunchedEffect(tab) {
        isLoading = true
        try {
            val res = withContext(Dispatchers.IO) { NewsApi.fetchTopHeadlines() }
            articles = res
        } catch (_: Throwable) { articles = emptyList() } finally { isLoading = false }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Noticias destacadas", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.zIndex(1f).weight(1f))
    val __titles = remember(articles) { articles.count { !((it.title?: "").isBlank()) } }
    val __descs = remember(articles) { articles.count { !((""?: "").isBlank()) } }
        }
        Spacer(Modifier.height(8.dp))

        ScrollableTabRow(
            selectedTabIndex = tab,
            edgePadding = 0.dp,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            tabs.forEachIndexed { i, label ->
                Tab(selected = tab == i, onClick = { tab = i }, text = { Text(label) })
            }
        }

        Spacer(Modifier.height(12.dp))

        if (isLoading) {
            Box(Modifier.fillMaxWidth().height(220.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Column
        }

        if (tab == 2) {
            MarketsStrip()
            Spacer(Modifier.height(16.dp))
        }

        if (articles.isNotEmpty()) {
            NewsHeroCard(articles.first(),
                onOpen = { url -> url?.let { ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it))) } },
                onRefresh = { tab = tab }
            )
            Spacer(Modifier.height(12.dp))
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth().heightIn(min = 220.dp, max = 480.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 4.dp)
        ) {
            val rest = if (articles.size > 1) articles.drop(1) else emptyList()
            itemsIndexed(rest) { _, a -> NewsCompactCard(a) { url ->
                url?.let { ctx.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it))) }
            } }
        }
    }
}

@Composable
private fun NewsHeroCard(article: NewsItem, onOpen: (String?) -> Unit, onRefresh: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(Modifier.padding(12.dp)) {
            if (!article.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = article.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().height(220.dp).clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(10.dp))
            }
            val ctx = LocalContext.current
            Text(article.title ?: "(no title)", style = MaterialTheme.typography.titleMedium,
                 maxLines = 3, overflow = TextOverflow.Ellipsis)
            IconButton(onClick = { Reader.speak(ctx,  article.title ?: "") }) { Icon(Icons.Filled.PlayArrow, contentDescription = "Read") }
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
    Card(modifier = Modifier.fillMaxWidth().clickable { onOpen(article.url) }, shape = RoundedCornerShape(14.dp)) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(article.title ?: "(no title)", style = MaterialTheme.typography.bodyLarge,
                     maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(4.dp))
                MetaRow(article.source, null)
            }
            Spacer(Modifier.width(12.dp))
            Box(Modifier.size(56.dp).clip(RoundedCornerShape(10.dp))) {
                if (!article.imageUrl.isNullOrBlank()) {
                    AsyncImage(model = article.imageUrl, contentDescription = null,
                               contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                } else {
                    Box(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)))
                }
            }
        }
    }
}

@Composable
private fun MarketsStrip() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf("AAPL +1.2%", "NVDA +0.8%", "MSFT -0.3%", "TSLA +2.4%", "GOOGL +0.1%").forEach { t ->
            Surface(shape = RoundedCornerShape(12.dp), tonalElevation = 1.dp) {
                Text(t, modifier = Modifier.zIndex(1f).padding(horizontal = 10.dp, vertical = 6.dp),
                     style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
private fun Modifier.bottomScrim(): Modifier = this.drawWithContent {
  drawContent()
  drawRect(
    Brush.verticalGradient(
      colors = listOf(
        Color.Black.copy(alpha = 0.00f),
        Color.Black.copy(alpha = 0.40f),
        Color.Black.copy(alpha = 0.88f)
      )
    )
  )
}

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

private fun tabToParams(tab: Int): Pair<String?, String?> = when (tab) {
  0 -> "general" to null
  1 -> "technology" to null
  2 -> "business" to null
  3 -> "general" to null
  4 -> "sports" to null
  5 -> null to "crypto"
  else -> "general" to null
}

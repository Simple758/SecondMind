package com.secondmind.minimal.feature.news

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@Composable
fun NewsHubScreen() {
    val vm: NewsViewModel = viewModel()
    val st by vm.state.collectAsState()

    LaunchedEffect(Unit) { vm.refresh(force = false) }

    Column(Modifier.fillMaxSize()) {
        NewsTabs(current = st.tab, onTab = { vm.setTab(it) }, onRefresh = { vm.refresh(true) })
        when {
            st.loading -> {
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }
            st.error != null -> {
                Text("Error: ${'$'}{st.error}", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
                TextButton(onClick = { vm.refresh(true) }, modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text("Retry")
                }
            }
            else -> {
                NewsList(st.articles)
            }
        }
    }
}

@Composable
private fun NewsTabs(current: NewsTab, onTab: (NewsTab) -> Unit, onRefresh: () -> Unit) {
    val tabs = listOf(
        NewsTab.ForYou to "For you",
        NewsTab.Tech to "Tech",
        NewsTab.Markets to "Markets",
        NewsTab.World to "World",
        NewsTab.Sports to "Sports",
        NewsTab.Crypto to "Crypto"
    )
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ScrollableTabRow(selectedTabIndex = tabs.indexOfFirst { it.first == current }) {
            tabs.forEachIndexed { index, pair ->
                val selected = pair.first == current
                Tab(selected = selected, onClick = { onTab(pair.first) }, text = {
                    Text(pair.second)
                })
            }
        }
        TextButton(onClick = onRefresh) { Text("Refresh") }
    }
}

@Composable
private fun NewsList(itemsList: List<Article>) {
    val ctx = LocalContext.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(itemsList) { a ->
            ArticleRow(a) {
                a.url?.let { url ->
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    ctx.startActivity(i)
                }
            }
        }
    }
}

@Composable
private fun ArticleRow(a: Article, onOpen: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable { onOpen() }) {
        Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AsyncImage(
                model = a.urlToImage,
                contentDescription = a.title,
                modifier = Modifier.size(72.dp),
                contentScale = ContentScale.Crop
            )
            Column(Modifier.weight(1f)) {
                Text(a.title ?: "", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))
                Text("${'$'}{a.source?.name ?: ""} • ${'$'}{timeAgo(a.publishedAt)}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

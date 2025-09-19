package com.secondmind.minimal.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeCarousel(modifier: Modifier = Modifier, onOpenNews: () -> Unit = {}) {
  LazyVerticalGrid(
    modifier = modifier.fillMaxSize(),
    columns = GridCells.Fixed(2),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(16.dp),
    ) {
    // Row 1 — News (full width)
    item(span = { GridItemSpan(2) }) {
      com.secondmind.minimal.feature.news.NewsCard(
        Modifier.fillMaxWidth(),
        onOpen = { sheetOpen.value = true }
      ),
        onOpen = { sheetOpen.value = true }
      )
    }
    // Row 2 — Quick Note + Brain Food
    item { com.secondmind.minimal.ui.components.QuickNoteCard() }
    item { com.secondmind.minimal.feature.wiki.WikiBrainFoodCard() }
    // Row 3 — Telegram + Watch Later
    item { com.secondmind.minimal.feature.tg.TelegramCard() }
    item { com.secondmind.minimal.feature.youtube.YtWatchLaterCard() }
    // Row 4 — Notif diag (full width)
    item(span = { GridItemSpan(2) }) {
      com.secondmind.minimal.home.NotifDiagRow(Modifier.fillMaxWidth())
    }
    // Row 5 — Notification access banner (full width)
    item(span = { GridItemSpan(2) }) {
      com.secondmind.minimal.ui.components.NotificationAccessBanner(Modifier.fillMaxWidth())
    }
  }
}

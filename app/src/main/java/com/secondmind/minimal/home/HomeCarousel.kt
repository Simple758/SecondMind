package com.secondmind.minimal.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeCarousel(modifier: Modifier = Modifier) {
  // Horizontal, swipeable row of cards. Height is bounded to avoid nested-scroll issues.
  LazyRow(
    modifier = modifier.heightIn(min = 0.dp, max = 240.dp),
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    contentPadding = PaddingValues(horizontal = 16.dp)
  ) {
    // Order these however you like:
    item { com.secondmind.minimal.feature.news.NewsCard() }
    item { com.secondmind.minimal.feature.tg.TelegramCard() }
    item { com.secondmind.minimal.feature.wiki.WikiBrainFoodCard() }
    item { com.secondmind.minimal.feature.youtube.YtWatchLaterCard() }
  }
}

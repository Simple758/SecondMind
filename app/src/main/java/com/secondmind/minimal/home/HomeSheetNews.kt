package com.secondmind.minimal.home
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import com.secondmind.minimal.news.NewsPanel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSheetNews(onDismiss: () -> Unit) {
  ModalBottomSheet(onDismissRequest = onDismiss) {
    NewsPanel(modifier = Modifier.fillMaxSize())
  }
}

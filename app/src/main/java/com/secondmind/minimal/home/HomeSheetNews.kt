package com.secondmind.minimal.home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import com.secondmind.minimal.news.NewsPanel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSheetNews(onDismiss: () -> Unit) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  LaunchedEffect(Unit) { sheetState.expand() }
  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState
  ) {
androidx.compose.runtime.CompositionLocalProvider(androidx.compose.material3.LocalContentColor provides androidx.compose.material3.MaterialTheme.colorScheme.onSurface) {
  androidx.compose.material3.ProvideTextStyle(androidx.compose.material3.MaterialTheme.typography.bodyMedium.copy(color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface)) {
    NewsPanel(modifier = Modifier.fillMaxSize())
  }
}
  }
}

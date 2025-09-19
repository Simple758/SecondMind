package com.secondmind.minimal.feature.news

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.secondmind.minimal.tts.Reader

@Composable
fun NewsCard(
  modifier: Modifier = Modifier,
  onOpen: () -> Unit = {}
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp)
      .heightIn(min = 64.dp, max = 96.dp)
      .clickable { onOpen() },
    shape = RoundedCornerShape(16.dp)
  ) {
    
Row(
      Modifier.fillMaxSize().padding(horizontal = 16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
            val ctx = LocalContext.current
Column(Modifier.weight(1f)) {
        Text("News", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(4.dp))
        // Simple teaser so the card is not empty; full headlines are inside the sheet.
        Text(
          "Latest headlines • tap to open",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1
        )
      }
      // Play uses default TRS settings (Reader) – speaks a short prompt
      IconButton(onClick = { Reader.speak(ctx,  "Opening news feed") }) {
        Icon(Icons.Filled.PlayArrow, contentDescription = "Read")
      }
      Icon(Icons.Filled.ArrowForward, contentDescription = "Open news")
    }
  }
}

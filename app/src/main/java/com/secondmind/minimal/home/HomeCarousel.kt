package com.secondmind.minimal.home

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.secondmind.minimal.inbox.InboxStore
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun HomeCarousel(
    modifier: Modifier = Modifier,
    onOpenInboxFallback: () -> Unit = {}
) {
    val ctx = LocalContext.current
    val conf = LocalConfiguration.current
    val screenW = conf.screenWidthDp.dp
    val cardWidth = remember(screenW) { screenW - 48.dp }
    val state = remember { LazyListState() }
    val scope = rememberCoroutineScope()

    var showNote by remember { mutableStateOf(false) }

    // TTS small helper (local, safe)
    val tts = remember {
        TextToSpeech(ctx) { it -> /* 0=SUCCESS */ }
    }
    DisposableEffect(Unit) { onDispose { runCatching { tts.shutdown() } } }

    // derive inbox info
    val items by InboxStore.items.collectAsState()
    val total = items.size

    val cards = remember(total) {
        listOf(
            CarouselCard(
                title = "Quick Note",
                subtitle = "Capture a thought quickly",
                iconRes = android.R.drawable.ic_menu_edit
            ) { showNote = true },
            CarouselCard(
                title = "Inbox",
                subtitle = if (total == 1) "1 notification" else "$total notifications",
                iconRes = android.R.drawable.ic_dialog_info
            ) {
                // Speak a short snapshot; if you want navigation, wire a lambda from Main later.
                val topLine = items.take(3).joinToString(". ") { it.appLabel + ": " +
                        (if (it.title.isNotBlank()) "${it.title}. " else "") + it.text }
                    .ifBlank { "No notifications yet" }
                runCatching {
                    tts.language = Locale.getDefault()
                    tts.speak(topLine, TextToSpeech.QUEUE_FLUSH, null, "speak_inbox")
                }
                // Optional: call a provided navigation fallback
                onOpenInboxFallback()
            }
        )
    }

    Column(modifier) {
        LazyRow(
            state = state,
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(cards) { idx, card ->
                Card(
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .width(cardWidth)
                        .height(160.dp)
                ) {
                    // Pretty background gradient
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.10f),
                                    )
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Column(Modifier.fillMaxSize()) {
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(id = card.iconRes),
                                        contentDescription = null
                                    )
                                    Spacer(Modifier.width(10.dp))
                                    Text(card.title,
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }
                                TextButton(onClick = { card.onClick() }) { Text("Open") }
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(
                                card.subtitle,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                            )
                            Spacer(Modifier.weight(1f))
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = { card.onClick() },
                                    shape = RoundedCornerShape(20.dp)
                                ) { Text("Go") }
                            }
                        }
                    }
                }
            }
        }

        // Dots indicator (based on first visible index)
        Spacer(Modifier.height(8.dp))
        val firstIdx = state.firstVisibleItemIndex
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(cards.size) { i ->
                val active = i == firstIdx
                Box(
                    Modifier
                        .size(if (active) 10.dp else 8.dp)
                        .padding(4.dp)
                        .background(
                            color = if (active) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(50)
                        )
                )
            }
        }
    }

    // Quick note dialog
    QuickNoteDialog(show = showNote, onDismiss = { showNote = false })
}

private data class CarouselCard(
    val title: String,
    val subtitle: String,
    val iconRes: Int,
    val onClick: () -> Unit
)

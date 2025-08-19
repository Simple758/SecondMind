package com.secondmind.minimal.ui

import android.content.Intent
import android.provider.Settings
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.secondmind.minimal.inbox.InboxStore
import com.secondmind.minimal.inbox.NotificationItem
import com.secondmind.minimal.inbox.MutedRegistry
import com.secondmind.minimal.tts.TtsSpeaker
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun InboxScreen() {
    val ctx = LocalContext.current
    val items by InboxStore.items.collectAsState()
    var tab by rememberSaveable { mutableStateOf(0) } // 0=All 1=Recent 2=Muted
    var muted by rememberSaveable { mutableStateOf(MutedRegistry.all(ctx)) }

    val groups = remember(items) {
        items.groupBy { it.pkg }.map { (pkg, list) ->
            val latest = list.maxByOrNull { it.ts }
            AppGroup(pkg = pkg, appLabel = latest?.appLabel ?: pkg, count = list.size, latest = latest)
        }.sortedByDescending { it.latest?.ts ?: 0L }
    }

    val filtered = when (tab) {
        1 -> groups // "Recent" is same order; could add time windows later
        2 -> groups.filter { muted.contains(it.pkg) }
        else -> groups.filter { !muted.contains(it.pkg) }
    }

    val speaker = remember(ctx) { TtsSpeaker(ctx) }
    DisposableEffect(Unit) { onDispose { speaker.shutdown() } }

    Column(Modifier.fillMaxSize().padding(12.dp)) {

        // Header chips (Notif access / A11y)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AssistChip(onClick = {
                ctx.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }, label = { Text("Notif access") })
            AssistChip(onClick = {
                ctx.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }, label = { Text("A11y settings") })
        }

        Spacer(Modifier.height(8.dp))

        Text("Inbox", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(4.dp))

        // Tabs
        TabRow(selectedTabIndex = tab) {
            listOf("All","Recent","Muted").forEachIndexed { i, name ->
                Tab(selected = tab==i, onClick = { tab = i }, text = { Text(name) })
            }
        }

        Spacer(Modifier.height(6.dp))

        // Top right actions
        Row(
            Modifier.fillMaxWidth().padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = {
                val text = buildString {
                    filtered.take(6).forEach { g ->
                        append(g.appLabel).append(". ")
                        g.latest?.let { l ->
                            if (l.title.isNotBlank()) append(l.title).append(". ")
                            append(l.text).append(". ")
                        }
                    }
                }
                speaker.speak(text)
            }) { Text("Speak All") }
            Spacer(Modifier.width(8.dp))
            TextButton(onClick = { InboxStore.clear() }) { Text("Clear") }
        }

        if (filtered.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No notifications yet", style = MaterialTheme.typography.bodyLarge)
            }
            return
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            items(filtered) { g ->
                AppGroupCard(
                    group = g,
                    muted = muted.contains(g.pkg),
                    onSpeak = {
                        val text = buildString {
                            append(g.appLabel).append(". ")
                            g.latest?.let { l ->
                                if (l.title.isNotBlank()) append(l.title).append(". ")
                                append(l.text)
                            }
                        }
                        speaker.speak(text)
                    },
                    onOpen = {
                        try {
                            val i = ctx.packageManager.getLaunchIntentForPackage(g.pkg)
                            if (i != null) ctx.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                        } catch (_:Throwable) {}
                    },
                    onToggleMute = {
                        muted = MutedRegistry.toggle(ctx, g.pkg)
                    }
                )
            }
        }
    }
}

private data class AppGroup(
    val pkg: String,
    val appLabel: String,
    val count: Int,
    val latest: NotificationItem?
)

@Composable
private fun AppGroupCard(
    group: AppGroup,
    muted: Boolean,
    onSpeak: () -> Unit,
    onOpen: () -> Unit,
    onToggleMute: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.fillMaxWidth().padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AppIcon(pkg = group.pkg, size = 36.dp)
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(group.appLabel, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.width(8.dp))
                        Text("•  ${group.count}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    val preview = buildString {
                        group.latest?.let { l ->
                            if (l.title.isNotBlank()) append(l.title).append(" — ")
                            append(l.text)
                        }
                    }.ifBlank { "(no text)" }
                    Text(
                        preview,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.width(8.dp))
                IconButton(onClick = onSpeak) {
                    Icon(Icons.Rounded.VolumeUp, contentDescription = "Speak")
                }
                IconButton(onClick = onOpen) {
                    Icon(Icons.Rounded.OpenInNew, contentDescription = "Open")
                }
            }
            Spacer(Modifier.height(6.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                val label = if (muted) "Unmute" else "Mute"
                TextButton(onClick = onToggleMute) { Text(label) }
            }
        }
    }
}

@Composable
private fun AppIcon(pkg: String, size: Dp) {
    val ctx = LocalContext.current
    val px = with(LocalDensity.current) { size.roundToPx() }
    var bmp by remember(pkg) { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(pkg, px) {
        val d = try { ctx.packageManager.getApplicationIcon(pkg) } catch (_:Throwable) { null }
        if (d != null) {
            val b = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888)
            val c = Canvas(b)
            d.setBounds(0, 0, px, px)
            d.draw(c)
            bmp = b
        } else {
            bmp = null
        }
    }

    if (bmp != null) {
        Image(bmp!!.asImageBitmap(), contentDescription = null,
            modifier = Modifier.size(size).clip(CircleShape))
    } else {
        Box(
            modifier = Modifier.size(size).clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
    }
}

/** Compatibility overload for older call sites. */
@Composable
fun InboxScreen(vararg unused: Any?) = InboxScreen()

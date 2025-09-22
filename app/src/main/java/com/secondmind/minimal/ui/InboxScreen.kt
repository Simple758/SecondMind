package com.secondmind.minimal.ui
import androidx.compose.ui.graphics.Color

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.secondmind.minimal.inbox.InboxStore
import com.secondmind.minimal.tts.TtsSpeaker

@Composable
fun InboxScreen() {
    val ctx = LocalContext.current
    val items by InboxStore.items.collectAsState()
    val groups = remember(items) { items.groupBy { it.pkg } }

    Column(Modifier.fillMaxSize().padding(16.dp)) {

        // Quick settings chips
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AssistChip("Notif access") {
                ctx.startActivity(
                    Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
            AssistChip("A11y settings") {
                ctx.startActivity(
                    Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }

        Spacer(Modifier.height(8.dp))
        Text("Inbox", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(8.dp))

        // Speak All / Clear
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = {
                val joined = items.take(30).joinToString(". ") {
                    val t = listOf(it.title, it.text)
                        .filter { s -> !s.isNullOrBlank() }
                        .joinToString(" — ")
                    if (t.isBlank()) "(no text)" else t
                }
                TtsSpeaker.speak(ctx, joined)
            }) { Text("Speak All") }

            TextButton(onClick = { InboxStore.clear() }) { Text("Clear") }
        }

        Spacer(Modifier.height(4.dp))

        if (groups.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No notifications", style = MaterialTheme.typography.bodyLarge)
            }
            return
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val sortedKeys = groups.keys.sorted()
            items(sortedKeys) { pkg ->
                val list = groups[pkg]!!.sortedByDescending { it.ts }
                val first = list.first()
                GroupCard(
                    packageName = pkg,
                    appLabel = first.appLabel.ifBlank { pkg },
                    count = list.size,
                    preview = buildString {
                        val t = first.title
                        val x = first.text
                        if (!t.isNullOrBlank()) append(t).append(" — ")
                        if (!x.isNullOrBlank()) append(x)
                    }.ifBlank { "(no text)" },
                    onSpeak = {
                        val say = list.take(10).joinToString(". ") { i ->
                            listOf(i.title, i.text)
                                .filter { s -> !s.isNullOrBlank() }
                                .joinToString(" — ")
                                .ifBlank { "(no text)" }
                        }
                        TtsSpeaker.speak(ctx, say)
                    },
                    onOpen = {
                        val launch = ctx.packageManager.getLaunchIntentForPackage(pkg)
                        if (launch != null) {
                            ctx.startActivity(launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun GroupCard(
    packageName: String,
    appLabel: String,
    count: Int,
    preview: String,
    onSpeak: () -> Unit,
    onOpen: () -> Unit
) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                AppIcon(packageName)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            appLabel,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text("  •  $count", style = MaterialTheme.typography.titleMedium)
                    }
                }
                // Actions (emoji to avoid extra deps)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onSpeak) { Text("🔊") }
                    TextButton(onClick = onOpen) { Text("↗") }
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                preview,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = { /* TODO: per-app mute list */ }) { Text("Mute") }
            }
        }
    }
}

@Composable
private fun AssistChip(label: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) { Text(label) }
}

@Composable
private fun AppIcon(packageName: String, size: Dp = 36.dp) {
    val ctx = LocalContext.current
    val density = ctx.resources.displayMetrics.density
    val sizePx = (size.value * density).toInt().coerceAtLeast(8)
    val iconBitmap by remember(packageName) {
        mutableStateOf(loadAppIconBitmap(ctx.packageManager, packageName, sizePx))
    }
    if (iconBitmap != null) {
        Image(
            bitmap = iconBitmap!!.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier.size(size).clip(CircleShape)
        )
    } else {
        Box(
            Modifier.size(size)
                .clip(CircleShape)
                .background(Color.Black)
        )
    }
}

// Small, dependency-free Drawable -> Bitmap
private fun loadAppIconBitmap(pm: PackageManager, pkg: String, sizePx: Int): Bitmap? {
    return try {
        val dr: Drawable = pm.getApplicationIcon(pkg)
        when (dr) {
            is BitmapDrawable -> Bitmap.createScaledBitmap(dr.bitmap, sizePx, sizePx, true)
            else -> {
                val bmp = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
                val c = Canvas(bmp)
                dr.setBounds(0, 0, sizePx, sizePx)
                dr.draw(c)
                bmp
            }
        }
    } catch (_: Throwable) { null }
}

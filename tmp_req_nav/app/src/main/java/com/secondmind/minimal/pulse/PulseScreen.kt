package com.secondmind.minimal.pulse

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import java.util.concurrent.TimeUnit

data class Signal(val title: String, val priority: Int, val dueAt: Long?, val createdAt: Long)

@Composable
fun PulseScreen() {
    val now = System.currentTimeMillis()
    val signals = remember {
        listOf(
            Signal("Review open positions", 3, now + 2*60*60*1000, now - 6*60*60*1000),
            Signal("Astro window tonight", 2, now + 10*60*60*1000, now - 60*60*1000),
            Signal("Water reminder", 1, null, now - 30*60*1000),
            Signal("Read market brief", 1, now + 4*60*60*1000, now - 2*60*60*1000),
        ).sortedWith(compareByDescending<Signal> { it.priority }
            .thenBy { it.dueAt ?: Long.MAX_VALUE }
            .thenBy { it.createdAt })
    }

    var lastWhisperAt by remember { mutableStateOf(0L) }
    val whisper = remember(lastWhisperAt) {
        val elapsed = now - lastWhisperAt
        if (elapsed > TimeUnit.HOURS.toMillis(24)) "Tip: long-press a signal to snooze it."
        else null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        Text("Pulse — Top 3 Signals")
        Spacer(Modifier.height(8.dp))
        signals.take(3).forEach { s ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text("• ${s.title}")
                    Text("priority=${s.priority}  due=${s.dueAt?.let { friendlyEta(it - now) } ?: "—"}")
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        if (whisper != null) {
            Card { Text(whisper, modifier = Modifier.padding(12.dp)) }
            lastWhisperAt = now
        }

        Spacer(Modifier.height(24.dp))
        Text("Constellation")
        Spacer(Modifier.height(8.dp))
        ConstellationMap(
            nodes = list_of_nodes(),
            edges = list_of_edges()
        )
    }
}

private fun list_of_nodes() = listOf(
    Node("You", 0f, 0f, 10f),
    Node("Trade", -60f, -10f, 6f),
    Node("Astro", 50f, -20f, 7f),
    Node("Health", -20f, 40f, 5f),
    Node("Learn", 40f, 30f, 5f),
)

private fun list_of_edges() = listOf(
    Edge(0,1), Edge(0,2), Edge(0,3), Edge(0,4),
    Edge(1,2), Edge(2,4)
)

private fun friendlyEta(deltaMs: Long): String {
    val absMs = abs(deltaMs)
    val h = absMs / (60*60*1000)
    val m = (absMs / (60*1000)) % 60
    val sign = if (deltaMs >= 0) "in " else ""
    return "$sign${h}h ${m}m"
}

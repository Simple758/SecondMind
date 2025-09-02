package com.secondmind.minimal.pulse

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp

data class Node(val label: String, val x: Float, val y: Float, val r: Float)
data class Edge(val a: Int, val b: Int)

@Composable
fun ConstellationMap(nodes: List<Node>, edges: List<Edge>) {
    Canvas(modifier = Modifier.fillMaxWidth().height(220.dp)) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        edges.forEach { e ->
            val n1 = nodes[e.a]; val n2 = nodes[e.b]
            drawLine(
                start = Offset(cx + n1.x, cy + n1.y),
                end = Offset(cx + n2.x, cy + n2.y)
            )
        }
        nodes.forEach { n ->
            drawCircle(center = Offset(cx + n.x, cy + n.y), radius = n.r)
        }
    }
}

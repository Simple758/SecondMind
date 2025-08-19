package com.secondmind.minimal.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Shapes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

private val NavyDarkColors = darkColorScheme(
    primary = Color(0xFFBBA7FF),
    onPrimary = Color(0xFF1A1A22),
    secondary = Color(0xFF8AD1FF),
    onSecondary = Color(0xFF0E1726),
    background = Color(0xFF0B1633),
    onBackground = Color(0xFFE7EAF3),
    surface = Color(0xFF0E1C3D),
    onSurface = Color(0xFFE7EAF3),
    surfaceVariant = Color(0xFF1A2A55),
    onSurfaceVariant = Color(0xFFC7D0F1),
    outline = Color(0xFF6E7AA8)
)

private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(10.dp),
    small      = RoundedCornerShape(14.dp),
    medium     = RoundedCornerShape(18.dp),
    large      = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun SecondMindMinimalTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NavyDarkColors,
        shapes = AppShapes,
        content = content
    )
}

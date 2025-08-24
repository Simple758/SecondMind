package com.secondmind.minimal.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

// Lavender accent and deep purple-gray surfaces (matches mock)
private val DeepNightColors = darkColorScheme(
  primary = Color(0xFFBBA7FF),      // lavender accent for chips/buttons
  onPrimary = Color(0xFF1C162A),

  secondary = Color(0xFF9E8CFF),
  onSecondary = Color(0xFF1C162A),

  background = Color(0xFF14121A),   // app background
  onBackground = Color(0xFFEAE6F7),

  surface = Color(0xFF1A1823),      // cards
  onSurface = Color(0xFFEAE6F7),

  surfaceVariant = Color(0xFF242031), // tab bar / subtle panels
  onSurfaceVariant = Color(0xFFCFC8E8),

  outline = Color(0xFF6F6887)
)

private val AppShapes = Shapes(
  extraSmall = RoundedCornerShape(10.dp),
  small      = RoundedCornerShape(14.dp),
  medium     = RoundedCornerShape(18.dp),
  large      = RoundedCornerShape(24.dp),  // rounder cards, like mock
  extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun SecondMindMinimalTheme(content: @Composable () -> Unit) {
  MaterialTheme(
    colorScheme = DeepNightColors,
    shapes = AppShapes,
    content = content
  )
}

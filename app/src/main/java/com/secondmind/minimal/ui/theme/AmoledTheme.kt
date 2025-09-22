package com.secondmind.minimal.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Pure black surfaces for AMOLED; gentle accent colors.
val AmoledColorScheme = darkColorScheme(
  primary = Color(0xFF80CBC4),
  onPrimary = Color(0xFF000000),
  secondary = Color(0xFF90CAF9),
  onSecondary = Color(0xFF000000),

  background = Color(0xFF000000),
  onBackground = Color(0xFFFFFFFF),

  surface = Color(0xFF000000),
  onSurface = Color(0xFFFFFFFF),

  surfaceVariant = Color(0xFF000000),
  onSurfaceVariant = Color(0xFFE0E0E0),

  error = Color(0xFFCF6679),
  onError = Color(0xFF000000)
)

@Composable
fun AmoledTheme(content: @Composable () -> Unit) {
  MaterialTheme(
    colorScheme = AmoledColorScheme,
    content = content
  )
}

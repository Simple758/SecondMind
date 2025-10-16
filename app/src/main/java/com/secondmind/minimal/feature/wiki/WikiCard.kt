package com.secondmind.minimal.feature.wiki

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.secondmind.minimal.ui.NavigationRoutes

/**
 * Minimal, compile-clean card that navigates to the in-app Wiki screen.
 * Works with either navController or onOpen() callback.
 */
@Composable
fun WikiBrainFoodCard(
    modifier: Modifier = Modifier,
    navController: NavController? = null,
    onOpen: (() -> Unit)? = null
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                when {
                    navController != null -> navController.navigate(NavigationRoutes.WIKI)
                    onOpen != null -> onOpen()
                    else -> { /* no-op */ }
                }
            },
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Wikipedia", style = MaterialTheme.typography.titleMedium)
            Text("Ask a quick fact from Wikipedia.", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

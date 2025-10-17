package com.secondmind.minimal.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.launch

data class DrawerItem(val label: String, val icon: ImageVector, val route: String)

private val items = listOf(
    DrawerItem("Home", Icons.Filled.Home, NavigationRoutes.HOME),
    DrawerItem("News", Icons.Filled.Article, NavigationRoutes.NEWS),
    DrawerItem("Markets", Icons.Filled.TrendingUp, NavigationRoutes.MARKETS),
    DrawerItem("Inbox", Icons.Filled.Mail, NavigationRoutes.INBOX),
    DrawerItem("Settings", Icons.Filled.Settings, NavigationRoutes.SETTINGS),
    DrawerItem("AI", Icons.Filled.SmartToy, "ai"),
    DrawerItem("Developer", Icons.Filled.Build, NavigationRoutes.DEVELOPER),
DrawerItem("Audiobooks", Icons.Filled.MenuBook, NavigationRoutes.AUDIOBOOKS),
)

@Composable
fun DrawerContent(
    selectedRoute: String,
    onDestinationClicked: (String) -> Unit,
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    ModalDrawerSheet(modifier = modifier) {
        items.forEach { item ->
            NavigationDrawerItem(
                icon = { androidx.compose.material3.Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = item.route == selectedRoute,
                onClick = {
                    scope.launch { drawerState.close() }
                    onDestinationClicked(item.route)
                }
            )
        }
    }
}

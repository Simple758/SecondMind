package com.secondmind.minimal.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.secondmind.minimal.data.NotificationEntity
import com.secondmind.minimal.inbox.InboxViewModel

@Composable
fun inboxViewModel(): InboxViewModel {
  val app = LocalContext.current.applicationContext as Application
  return viewModel(factory = InboxViewModel.factory(app))
}

@Composable
fun InboxScreen(nav: NavController, vm: InboxViewModel = inboxViewModel()) {
  val ctx = LocalContext.current
  val list by vm.notifications.collectAsState()

  Column(Modifier.fillMaxSize()) {
    if (!hasNotificationAccess(ctx)) {
      PermissionCard(
        title = "Notification access",
        subtitle = "Enable to see your notifications here",
        action = "Open settings"
      ) { ctx.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)) }
    }
    Row(Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
      Text("Inbox (${list.size})", style = MaterialTheme.typography.titleMedium)
      OutlinedButton(onClick = { vm.clear() }) { Text("Clear") }
    }
    Divider()
    if (list.isEmpty()) {
      Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("No notifications yet") }
    } else {
      LazyColumn(Modifier.fillMaxSize()) {
        items(list, key = { it.id }) { n ->
          InboxRow(n, onOpen = { nav.navigate("notification/${n.id}") }, onDelete = { vm.delete(n.id) })
          Divider()
        }
      }
    }
  }
}

@Composable
private fun PermissionCard(title: String, subtitle: String, action: String, onClick: () -> Unit) {
  Card(Modifier.fillMaxWidth().padding(12.dp)) {
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Text(title, style = MaterialTheme.typography.titleMedium)
      Text(subtitle, style = MaterialTheme.typography.bodyMedium)
      OutlinedButton(onClick = onClick) { Text(action) }
    }
  }
}

@Composable
private fun InboxRow(n: NotificationEntity, onOpen: () -> Unit, onDelete: () -> Unit) {
  Row(
    Modifier.fillMaxWidth().clickable { onOpen() }.padding(12.dp),
    horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
  ) {
    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
      Text(n.title ?: appFromPackage(LocalContext.current, n.appPackage), style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
      Text(n.text ?: " ", style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
      Text(n.appPackage, style = MaterialTheme.typography.labelSmall)
    }
    OutlinedButton(onClick = onDelete) { Text("Delete") }
  }
}

private fun appFromPackage(ctx: Context, pkg: String): String =
  try { ctx.packageManager.getApplicationLabel(ctx.packageManager.getApplicationInfo(pkg, 0)).toString() } catch (_: Throwable) { pkg }

private fun hasNotificationAccess(ctx: Context): Boolean {
  val enabled = Settings.Secure.getString(ctx.contentResolver, "enabled_notification_listeners") ?: return false
  return enabled.contains(ctx.packageName)
}

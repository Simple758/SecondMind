@Composable
fun HomeScreen(onSettings: () -> Unit, onInbox: () -> Unit, onOpenNews: () -> Unit) {
  Column(Modifier.fillMaxSize()) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      OutlinedButton(onClick = onInbox) {
        Text("Inbox")
      }
      OutlinedButton(onClick = onSettings) {
        Text("Settings")
      }
    }
    HomeCarousel(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f), // this makes the grid take up the rest of the screen
      onOpenNews = onOpenNews
    )
  }
}
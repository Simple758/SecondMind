package com.secondmind.minimal.future.ui
import androidx.compose.foundation.layout.*; import androidx.compose.material3.*; import androidx.compose.runtime.*; import androidx.compose.ui.Modifier; import androidx.compose.ui.unit.dp; import androidx.navigation.NavController
import com.secondmind.minimal.future.db.*; import com.secondmind.minimal.future.eval.FutureSeedEvaluator; import kotlinx.coroutines.Dispatchers; import kotlinx.coroutines.withContext; import androidx.compose.ui.platform.LocalContext
@Composable
fun PulseScreen(nav:NavController){
  val ctx=LocalContext.current
  var top by remember{ mutableStateOf(listOf<SignalEntity>()) }
  LaunchedEffect(Unit){ withContext(Dispatchers.IO){ FutureSeedEvaluator.evaluateNow(ctx); top=FutureSignalRepo.top3(ctx) } }
  val notes by FutureNoteRepo.recent(ctx,3).collectAsState(initial= emptyList())
  val whisper = remember(notes){ notes.firstOrNull()?.text?.take(80) }
  Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement=Arrangement.spacedBy(12.dp)){
    Text("Pulse", style=MaterialTheme.typography.headlineSmall)
    if(whisper!=null) ElevatedCard{ Text(whisper, Modifier.padding(12.dp)) } else Text("No whisper yet.")
    Text("Top Signals", style=MaterialTheme.typography.titleMedium)
    if(top.isEmpty()) Text("No signals yet.") else top.forEach{
      ElevatedCard(Modifier.fillMaxWidth()){ Column(Modifier.padding(12.dp)){ Text(it.title, style=MaterialTheme.typography.titleMedium); Text("Source: ${it.source} • Priority ${it.priority}", style=MaterialTheme.typography.bodySmall) } }
    }
    Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){ OutlinedButton(onClick={ nav.navigate("seed/new") }){ Text("Add Seed") }; OutlinedButton(onClick={ nav.popBackStack() }){ Text("Back") } }
  }
}

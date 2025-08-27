package com.secondmind.minimal.future.ui
import androidx.compose.foundation.layout.*; import androidx.compose.material3.*; import androidx.compose.runtime.*; import androidx.compose.ui.Modifier; import androidx.compose.ui.platform.LocalContext; import androidx.compose.ui.unit.dp
import com.secondmind.minimal.future.engine.AppMode; import com.secondmind.minimal.future.engine.ModeEngine
@Composable
fun ModeChipBar(onPulse:()->Unit,onAddSeed:()->Unit,modifier:Modifier=Modifier){
  val ctx=LocalContext.current; var mode by remember{ mutableStateOf(ModeEngine.getOrAuto(ctx)) }
  Card(modifier.fillMaxWidth()){
    Column(Modifier.padding(12.dp), verticalArrangement=Arrangement.spacedBy(8.dp)){
      Text("Mode", style=MaterialTheme.typography.titleMedium)
      Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
        OutlinedButton(onClick={ mode=ModeEngine.getOrAuto(ctx) }){ Text("Auto • ${mode.name}") }
        Button(onClick={ mode=AppMode.Trader; ModeEngine.setManual(ctx,mode) }){ Text("Trader") }
        OutlinedButton(onClick={ mode=AppMode.Cosmic; ModeEngine.setManual(ctx,mode) }){ Text("Cosmic") }
        OutlinedButton(onClick={ mode=AppMode.Everyday; ModeEngine.setManual(ctx,mode) }){ Text("Everyday") }
      }
      Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
        OutlinedButton(onClick=onPulse){ Text("Open Pulse") }
        OutlinedButton(onClick=onAddSeed){ Text("Add Seed") }
      }
    }
  }
}

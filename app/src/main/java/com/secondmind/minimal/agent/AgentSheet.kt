package com.secondmind.minimal.agent
import androidx.compose.material3.*; import androidx.compose.runtime.*; import androidx.compose.foundation.layout.*; import androidx.compose.ui.Modifier; import androidx.compose.ui.unit.dp
@Composable fun AgentSheet(onDismiss:()->Unit,onOpenTab:(String)->Unit){ var text by remember{ mutableStateOf("") }
  AlertDialog(onDismissRequest=onDismiss,
    confirmButton={ TextButton(onClick={ if(text.contains("news",true)) onOpenTab("news"); onDismiss() }){ Text("Go") } },
    dismissButton={ TextButton(onClick=onDismiss){ Text("Close") } },
    title={ Text("SecondMind Agent") },
    text={ Column(Modifier.fillMaxWidth(), verticalArrangement=Arrangement.spacedBy(8.dp)){ Text("Try: \"Open news\", \"Summarize this\""); OutlinedTextField(value=text,onValueChange={text=it}, singleLine=true, modifier=Modifier.fillMaxWidth()) } })
}

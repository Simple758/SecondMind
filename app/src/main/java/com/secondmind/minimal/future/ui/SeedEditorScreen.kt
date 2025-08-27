package com.secondmind.minimal.future.ui
import androidx.compose.foundation.layout.*; import androidx.compose.material3.*; import androidx.compose.runtime.*; import androidx.compose.ui.Modifier; import androidx.compose.ui.platform.LocalContext; import androidx.compose.ui.unit.dp; import androidx.navigation.NavController
import com.secondmind.minimal.future.db.*
@Composable
fun SeedEditorScreen(nav:NavController){
  val ctx=LocalContext.current
  var text by remember{ mutableStateOf("") }
  var type by remember{ mutableStateOf("time") }
  var operator by remember{ mutableStateOf(">=") }
  var value by remember{ mutableStateOf("") }
  var keyword by remember{ mutableStateOf("") }
  var deadline by remember{ mutableStateOf("") }
  Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement=Arrangement.spacedBy(10.dp)){
    Text("New Seed", style=MaterialTheme.typography.titleLarge)
    OutlinedTextField(text, {text=it}, Modifier.fillMaxWidth(), label={ Text("Note (text)") })
    Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
      val sel=(type=="time"); (if(sel) Button(onClick={}) else OutlinedButton(onClick={ type="time" })){ Text("Time") }
      val sel2=(type=="price_manual"); (if(sel2) Button(onClick={}) else OutlinedButton(onClick={ type="price_manual" })){ Text("Price") }
      val sel3=(type=="keyword"); (if(sel3) Button(onClick={}) else OutlinedButton(onClick={ type="keyword" })){ Text("Keyword") }
      val sel4=(type=="context"); (if(sel4) Button(onClick={}) else OutlinedButton(onClick={ type="context" })){ Text("Context") }
    }
    if(type=="price_manual"){ OutlinedTextField(operator,{operator=it}, label={Text("Operator (>=, <=, >, <, ==)")}); OutlinedTextField(value,{value=it}, label={Text("Target (number)")}) }
    if(type=="keyword"){ OutlinedTextField(keyword,{keyword=it}, label={Text("Keyword")}) }
    if(type=="time"){ OutlinedTextField(deadline,{deadline=it}, label={Text("Deadline (epoch millis)")}) }
    Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){
      Button(onClick={ val id=FutureNoteRepo.add(ctx,text=isSeedText(text),isSeed=true); val dl=deadline.toLongOrNull(); FutureSeedRepo.addSeed(ctx,id,type,operator,value,keyword,dl,"PENDING"); nav.popBackStack() }){ Text("Save") }
      OutlinedButton(onClick={ nav.popBackStack() }){ Text("Cancel") }
    }
  }
}
private fun isSeedText(t:String)= if(t.isBlank()) "(seed)" else t

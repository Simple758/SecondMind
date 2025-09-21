package com.secondmind.minimal.insights
import android.content.Context; import com.secondmind.minimal.memory.MemoryStore
data class Theme(val label:String,val count:Int)
object InsightEngine{
  fun themes(ctx:Context,max:Int=3):List<Theme>{
    val titles=MemoryStore.recentNewsTitles(ctx,20); val f=mutableMapOf<String,Int>(); val sep=Regex("[^a-zA-Záéíóúüñ0-9]+")
    titles.forEach{ t-> t.lowercase().split(sep).filter{it.length>=4}.forEach{ w-> f[w]=(f[w]?:0)+1 } }
    return f.entries.sortedByDescending{it.value}.take(max).map{ Theme(it.key.replaceFirstChar{c->c.uppercase()},it.value) }
  }
}

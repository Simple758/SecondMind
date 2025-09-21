package com.secondmind.minimal.ai.offline
object OfflineNLP{
  private val stop=setOf("the","a","an","of","and","to","in","on","for","with","is","are","was","were","la","el","los","las","de","y","en","para","con","es","son")
  fun summarize(t:String,m:Int=3):String{ val s=t.split(Regex("(?<=[.!?])\s+")).filter{it.isNotBlank()}; if(s.size<=m)return t.trim()
    return s.map{ u-> u.lowercase().split(Regex("[^a-záéíóúüñ0-9]+")).filter{it.isNotBlank()&&it !in stop}.groupingBy{it}.eachCount().values.sum() to u }
      .sortedByDescending{it.first}.take(m).joinToString(" ") }
  fun keywords(t:String,m:Int=5)=t.lowercase().split(Regex("[^a-záéíóúüñ0-9]+")).filter{it.isNotBlank()&&it !in stop}
    .groupingBy{it}.eachCount().toList().sortedByDescending{it.second}.map{it.first}.distinct().take(m)
  fun sentiment(t:String):Int{ val p=listOf("gain","rally","beat","record","up","bull","positive","growth","strong","mejora","sube")
    val n=listOf("fall","drop","miss","down","bear","negative","decline","weak","crash","cae","baja"); var sc=0; val x=t.lowercase()
    p.forEach{ if(x.contains(it)) sc++ }; n.forEach{ if(x.contains(it)) sc-- }; return sc.coerceIn(-3,3) }
}

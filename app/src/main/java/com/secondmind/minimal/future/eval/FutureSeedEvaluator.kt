package com.secondmind.minimal.future.eval
import android.content.Context; import com.secondmind.minimal.future.db.*
object FutureSeedEvaluator{
  fun evaluateNow(ctx:Context){
    for(s in FutureSeedRepo.pending(ctx)){
      when(s.type){
        "time"-> { val d=s.deadline?:continue; if(System.currentTimeMillis()>=d) fire(ctx,s,"⏰ Time reached",2,d) }
        "price_manual"-> {
          val trg=s.value?.toDoubleOrNull()?:continue; val price=FutureUserSignalRepo.latest(ctx,"PRICE_INPUT",1).firstOrNull()?.value?.toDoubleOrNull()?:continue
          val op=(s.operator?:">="); val ok= when(op){ ">="->price>=trg; "<="->price<=trg; ">"->price>trg; "<"->price<trg; "=="->price==trg; else->false }
          if(ok) fire(ctx,s,"💹 Price target $op $trg",3,System.currentTimeMillis())
        }
        "keyword"-> { val kw=s.keyword?.trim()?.lowercase()?:continue; if(FutureUserSignalRepo.latest(ctx,"KEYWORD",5).any{ (it.value?:"").trim().lowercase()==kw }) fire(ctx,s,"🔎 Keyword matched: $kw",1,System.currentTimeMillis()) }
        "context"-> { val h=java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY); if(h in 5..10) fire(ctx,s,"📱 Morning context",1,System.currentTimeMillis()) }
      }
    }
  }
  private fun fire(ctx:Context,seed:SeedEntity,title:String,priority:Int,dueAt:Long?){ FutureSignalRepo.add(ctx,title,"seed",priority,dueAt); FutureSeedRepo.update(ctx,seed.copy(status="FIRED")) }
}

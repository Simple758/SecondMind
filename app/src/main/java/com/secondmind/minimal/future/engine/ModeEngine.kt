package com.secondmind.minimal.future.engine
import android.content.Context; import com.secondmind.minimal.future.db.*; import java.util.Calendar
enum class AppMode{Trader,Cosmic,Everyday}
object ModeEngine{
  fun infer(ctx:Context):AppMode{ val h=Calendar.getInstance().get(Calendar.HOUR_OF_DAY); val trader=(h in 7..22)
    val cosmicKW=FutureUserSignalRepo.latest(ctx,"KEYWORD",10).count{ (it.value?:"").contains("astro",true)}>=2
    val cosmicOpen=FutureUserSignalRepo.latest(ctx,"APP_USED",5).any{ (it.value?:"")=="Astro" && System.currentTimeMillis()-it.timestamp<86_400_000 }
    return when{ trader->AppMode.Trader; (cosmicKW||cosmicOpen)->AppMode.Cosmic; else->AppMode.Everyday }
  }
  fun getOrAuto(ctx:Context)= when(val m=FutureModeRepo.get(ctx).lastManualMode){ "Trader"->AppMode.Trader; "Cosmic"->AppMode.Cosmic; "Everyday"->AppMode.Everyday; else-> infer(ctx).also{ FutureModeRepo.set(ctx,it.name,false)} }
  fun setManual(ctx:Context,mode:AppMode)=FutureModeRepo.set(ctx,mode.name,true)
}

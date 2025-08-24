package com.secondmind.minimal.diag

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.system.exitProcess

object CrashLogger {
  fun install(ctx: Context) {
    val prev = Thread.getDefaultUncaughtExceptionHandler()
    Thread.setDefaultUncaughtExceptionHandler { t, e ->
      try {
        val ts = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val sw = StringWriter().also { e.printStackTrace(PrintWriter(it)) }
        val payload = buildString {
          appendLine("FATAL EXCEPTION @ $ts")
          appendLine("Thread: ${t.name}")
          appendLine(sw.toString())
        }
        saveToDownloads(ctx, "secondmind_crash_$ts.txt", payload)
      } catch (x: Throwable) {
        try { Log.e("CrashLogger", "write failed", x) } catch (_: Throwable) {}
      } finally {
        // chain to previous handler or exit
        prev?.uncaughtException(t, e) ?: exitProcess(2)
      }
    }
  }

  private fun saveToDownloads(ctx: Context, name: String, text: String): Uri? {
    return try {
      val values = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, name)
        put(MediaStore.Downloads.MIME_TYPE, "text/plain")
        if (Build.VERSION.SDK_INT >= 29) put(MediaStore.Downloads.IS_PENDING, 1)
      }
      val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
      val uri = ctx.contentResolver.insert(collection, values)
      if (uri != null) {
        ctx.contentResolver.openOutputStream(uri)?.use { it.write(text.toByteArray()) }
        if (Build.VERSION.SDK_INT >= 29) {
          values.clear(); values.put(MediaStore.Downloads.IS_PENDING, 0)
          ctx.contentResolver.update(uri, values, null, null)
        }
      }
      uri
    } catch (_: Throwable) { null }
  }
}

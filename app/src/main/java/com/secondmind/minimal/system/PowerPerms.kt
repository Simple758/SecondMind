package com.secondmind.minimal.system

import android.Manifest
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.os.Environment
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS
import android.os.Process
import androidx.core.content.ContextCompat

object PowerPerms {

  // ---- Dangerous (request via runtime prompt) ----
  @JvmField
  val DANGEROUS_CORE = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.RECORD_AUDIO,
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.READ_CONTACTS,
    Manifest.permission.WRITE_CONTACTS,
    Manifest.permission.READ_CALENDAR,
    Manifest.permission.WRITE_CALENDAR
  )

  fun missing(context: Context, perms: Array<String>): List<String> =
    perms.filter {
      ContextCompat.checkSelfPermission(
        context, it
      ) != android.content.pm.PackageManager.PERMISSION_GRANTED
    }

  // ---- Special access (require navigating to Settings) ----

  // All files access (MANAGE_EXTERNAL_STORAGE)
  fun hasAllFilesAccess(@Suppress("UNUSED_PARAMETER") context: Context): Boolean =
    if (Build.VERSION.SDK_INT >= 30) Environment.isExternalStorageManager() else true

  fun launchAllFilesAccess(context: Context) {
    if (Build.VERSION.SDK_INT >= 30) {
      try {
        context.startActivity(
          Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
            data = Uri.parse("package:${context.packageName}")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
          }
        )
      } catch (_: Throwable) {
        context.startActivity(
          Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
      }
    }
  }

  // Overlay
  fun canDrawOverlays(context: Context) = Settings.canDrawOverlays(context)
  fun launchOverlaySettings(context: Context) =
    context.startActivity(
      Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
        Uri.parse("package:${context.packageName}")
      ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    )

  // Write system settings
  fun canWriteSettings(context: Context) = Settings.System.canWrite(context)
  fun launchWriteSettings(context: Context) =
    context.startActivity(
      Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
        Uri.parse("package:${context.packageName}")
      ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    )

  // Usage access
  fun hasUsageAccess(context: Context): Boolean {
    return try {
      val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
      val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        appOps.unsafeCheckOpNoThrow(OPSTR_GET_USAGE_STATS, Process.myUid(), context.packageName)
      } else {
        appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, Process.myUid(), context.packageName)
      }
      mode == AppOpsManager.MODE_ALLOWED
    } catch (_: Throwable) {
      false
    }
  }
  fun launchUsageAccess(context: Context) =
    context.startActivity(
      Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    )

  // Ignore battery optimizations
  fun ignoringBatteryOptimizations(context: Context): Boolean {
    val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    return pm.isIgnoringBatteryOptimizations(context.packageName)
  }
  fun launchIgnoreBatteryOptimizations(context: Context) =
    context.startActivity(
      Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
        Uri.parse("package:${context.packageName}")
      ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    )

  // Exact alarms
  fun canScheduleExactAlarms(context: Context): Boolean =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
      am.canScheduleExactAlarms()
    } else true

  fun launchExactAlarmsSettings(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      context.startActivity(
        Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
          Uri.parse("package:${context.packageName}")
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      )
    }
  }

  // Unknown sources (install packages)
  fun canInstallUnknownSources(context: Context): Boolean =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
      context.packageManager.canRequestPackageInstalls()
    else true

  fun launchUnknownSources(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      context.startActivity(
        Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
          Uri.parse("package:${context.packageName}")
        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      )
    } else {
      context.startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
  }

  // Defaults (user must set you as default app to get full call/SMS powers)
  fun launchDefaultDialer(context: Context) =
    context.startActivity(Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

  fun launchDefaultSms(context: Context) =
    context.startActivity(Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
}

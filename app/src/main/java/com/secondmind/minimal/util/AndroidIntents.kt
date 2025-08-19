package com.secondmind.minimal.util

import android.content.Context
import android.content.Intent
import android.widget.Toast

fun openApp(context: Context, pkg: String) {
    val pm = context.packageManager
    val it = pm.getLaunchIntentForPackage(pkg)
    if (it != null) {
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(it)
    } else {
        Toast.makeText(context, "No launcher for $pkg", Toast.LENGTH_SHORT).show()
    }
}

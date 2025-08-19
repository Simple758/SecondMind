package com.secondmind.minimal.inbox

import android.content.Context

object MutedRegistry {
    private const val PREF = "sm_muted_pkgs"
    fun all(ctx: Context): Set<String> =
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .getStringSet("muted", emptySet()) ?: emptySet()

    fun isMuted(ctx: Context, pkg: String) = all(ctx).contains(pkg)

    fun toggle(ctx: Context, pkg: String, mute: Boolean? = null): Set<String> {
        val sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val cur = sp.getStringSet("muted", emptySet())?.toMutableSet() ?: mutableSetOf()
        val target = mute ?: !cur.contains(pkg)
        if (target) cur.add(pkg) else cur.remove(pkg)
        sp.edit().putStringSet("muted", cur).apply()
        return cur.toSet()
    }
}

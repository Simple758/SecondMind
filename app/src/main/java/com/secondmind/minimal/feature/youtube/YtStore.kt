package com.secondmind.minimal.feature.youtube

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.secondmind.minimal.feature.storage.JsonPrefs
import org.json.JSONArray
import org.json.JSONObject

object YtStore {
    private const val KEY_LIST = "yt.list"
    private const val KEY_META_PREFIX = "yt.meta."

    fun list(ctx: Context): List<YtItem> {
        val arr = JsonPrefs.getArray(ctx, KEY_LIST) ?: JSONArray()
        return (0 until arr.length()).mapNotNull {
            val id = arr.optString(it, null) ?: return@mapNotNull null
            YtItem(id, YtId.canonicalUrl(id))
        }
    }
    private fun saveList(ctx: Context, ids: List<String>) {
        val arr = JSONArray().apply { ids.forEach { put(it) } }
        JsonPrefs.put(ctx, KEY_LIST, arr)
    }

    fun addId(ctx: Context, id: String): Boolean {
        val ids = list(ctx).map { it.id }.toMutableList()
        if (ids.contains(id)) return false
        ids.add(0, id)
        if (ids.size > 50) ids.removeLast()
        saveList(ctx, ids)
        return true
    }
    fun remove(ctx: Context, id: String) {
        saveList(ctx, list(ctx).map { it.id }.filterNot { it == id })
        JsonPrefs.remove(ctx, KEY_META_PREFIX + id)
    }

    fun meta(ctx: Context, id: String): YtMeta? {
        val o = JsonPrefs.getObject(ctx, KEY_META_PREFIX + id) ?: return null
        val title = o.optString("title"); val author = o.optString("author"); val thumb = o.optString("thumb")
        if (title.isBlank() || thumb.isBlank()) return null
        return YtMeta(id, title, author, thumb)
    }
    fun putMeta(ctx: Context, m: YtMeta) {
        JsonPrefs.put(ctx, KEY_META_PREFIX + m.id,
            JSONObject().put("title", m.title).put("author", m.channel).put("thumb", m.thumbUrl)
        )
    }

    fun addFromClipboard(ctx: Context): String? {
        val cm = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val text = cm.primaryClip?.takeIf { it.itemCount > 0 }?.getItemAt(0)?.text?.toString() ?: return null
        val id = YtId.extract(text) ?: return null
        addId(ctx, id)
        return id
    }
}

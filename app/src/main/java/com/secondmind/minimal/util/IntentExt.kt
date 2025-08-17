package com.secondmind.minimal.util

import android.content.Intent
import android.os.Parcelable
import java.io.Serializable

fun Intent.putStringExtraSafe(key: String, value: String?) = apply {
    if (value != null) putExtra(key, value) else removeExtra(key)
}
fun Intent.putCharSequenceExtraSafe(key: String, value: CharSequence?) = apply {
    if (value != null) putExtra(key, value) else removeExtra(key)
}
fun Intent.putBooleanExtraSafe(key: String, value: Boolean?) = apply {
    if (value != null) putExtra(key, value) else removeExtra(key)
}
fun Intent.putIntExtraSafe(key: String, value: Int?) = apply {
    if (value != null) putExtra(key, value) else removeExtra(key)
}
fun Intent.putLongExtraSafe(key: String, value: Long?) = apply {
    if (value != null) putExtra(key, value) else removeExtra(key)
}
fun <T: Parcelable> Intent.putParcelableExtraSafe(key: String, value: T?) = apply {
    if (value != null) putExtra(key, value) else removeExtra(key)
}
fun <T: Serializable> Intent.putSerializableExtraSafe(key: String, value: T?) = apply {
    if (value != null) putExtra(key, value) else removeExtra(key)
}

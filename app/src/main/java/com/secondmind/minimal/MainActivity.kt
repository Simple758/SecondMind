package com.secondmind.minimal

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tv = TextView(this)
        tv.text = "SecondMind Minimal Base"
        tv.textSize = 20f
        setContentView(tv)
    }
}

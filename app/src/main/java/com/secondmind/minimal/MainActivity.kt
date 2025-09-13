package com.secondmind.minimal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tv = TextView(this)
        tv.text = "SecondMind Minimal Base"
        tv.textSize = 20f
        setContentView(tv)
    }
}

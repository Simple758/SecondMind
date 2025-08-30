package com.secondmind.minimal.v01.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.secondmind.minimal.v01.ui.components.ModeChip

@Composable
fun V01HomeScreen(mode: String = "Everyday", onModeClicked: () -> Unit = {}) {
    Column(modifier = Modifier.fillMaxSize()) {
        ModeChip(modeLabel = "Auto • " + mode, onClick = onModeClicked)
    }
}

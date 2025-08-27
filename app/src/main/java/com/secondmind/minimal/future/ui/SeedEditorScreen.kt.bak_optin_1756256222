package com.secondmind.minimal.future.ui
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun SeedEditorScreen(
    onBack: () -> Unit = {},
    onSave: (text: String, type: String, value: String, keyword: String, deadline: Long?) -> Unit = { _,_,_,_,_ -> }
) {
    var noteText by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("time") } // time | price_manual | keyword | context
    var value by remember { mutableStateOf("") }    // numeric for price_manual or generic value
    var keyword by remember { mutableStateOf("") }
    var deadlineStr by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seed Editor") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = noteText,
                onValueChange = { noteText = it },
                label = { Text("Note") },
                modifier = Modifier.fillMaxWidth()
            )

            // Type chooser (stable buttons, no experimental APIs)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TypeButton(current = type, mine = "time", label = "Time") { type = "time" }
                TypeButton(current = type, mine = "price_manual", label = "Price") { type = "price_manual" }
                TypeButton(current = type, mine = "keyword", label = "Keyword") { type = "keyword" }
                TypeButton(current = type, mine = "context", label = "Context") { type = "context" }
            }

            when (type) {
                "time" -> {
                    OutlinedTextField(
                        value = deadlineStr,
                        onValueChange = { deadlineStr = it },
                        label = { Text("Deadline (epoch ms)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                "price_manual" -> {
                    OutlinedTextField(
                        value = value,
                        onValueChange = { value = it },
                        label = { Text("Target price (number)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                "keyword" -> {
                    OutlinedTextField(
                        value = keyword,
                        onValueChange = { keyword = it },
                        label = { Text("Keyword to watch for") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                "context" -> {
                    OutlinedTextField(
                        value = value,
                        onValueChange = { value = it },
                        label = { Text("Context value (e.g., WIFI, HOME)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(onClick = onBack) { Text("Cancel") }
                Button(onClick = {
                    val deadline = deadlineStr.toLongOrNull()
                    onSave(noteText, type, value, keyword, deadline)
                    onBack()
                }) { Text("Save") }
            }
        }
    }
}

@Composable
private fun TypeButton(
    current: String,
    mine: String,
    label: String,
    onClick: () -> Unit
) {
    val selected = current == mine
    if (selected) {
        FilledTonalButton(onClick = onClick) { Text(label) }
    } else {
        OutlinedButton(onClick = onClick) { Text(label) }
    }
}

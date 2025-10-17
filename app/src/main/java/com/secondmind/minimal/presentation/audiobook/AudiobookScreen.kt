// ============================================
// FILE: presentation/audiobook/AudiobookScreen.kt
// ============================================

package com.secondmind.minimal.presentation.audiobook

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.secondmind.minimal.presentation.audiobook.components.BookCoverCard
import com.secondmind.minimal.presentation.audiobook.components.ChapterList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudiobookScreen(
    vm: AudiobookViewModel,
    onOpenPlayer: () -> Unit
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            uri?.let {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    (Intent.FLAG_GRANT_READ_URI_PERMISSION)
                )
                vm.importPdf(context, it)
            }
        }
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                picker.launch(arrayOf("application/pdf"))
            }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            if (state.isBusy) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            state.current?.let { book ->
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp)
                )
                BookCoverCard(book = book)
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = onOpenPlayer,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text("Open Player")
                }
                Spacer(Modifier.height(12.dp))
                ChapterList(chapters = book.chapters)
            } ?: run {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Import a PDF to get started")
                }
            }
        }
    }
}

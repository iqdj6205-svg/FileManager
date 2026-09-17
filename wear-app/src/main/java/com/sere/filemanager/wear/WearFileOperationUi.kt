package com.sere.filemanager.wear

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Text

@Composable
fun WearClipboardBanner(
    state: WearClipboardState,
    onPaste: () -> Unit,
    onClear: () -> Unit,
) {
    if (!state.hasEntry) return
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize().padding(10.dp),
        contentPadding = PaddingValues(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        item { Text("Clipboard", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) }
        item { Text("${state.mode}: ${state.fileName}", textAlign = TextAlign.Center) }
        item { Chip(label = { Text("Paste here") }, onClick = onPaste, modifier = Modifier.fillMaxWidth()) }
        item { Chip(label = { Text("Clear") }, onClick = onClear, modifier = Modifier.fillMaxWidth()) }
    }
}

@Composable
fun WearOperationHelpScreen(onBack: () -> Unit) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize().padding(10.dp),
        contentPadding = PaddingValues(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        item { Text("File actions", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) }
        item { Text("Tap folder to open. Tap file to open actions. Use Copy/Move then Paste in destination.", textAlign = TextAlign.Center) }
        item { Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back") } }
    }
}

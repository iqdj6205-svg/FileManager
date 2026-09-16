package com.sere.filemanager.wear

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import com.sere.filemanager.core.files.SortMode

@Composable
fun SortScreen(current: SortMode, onSelect: (SortMode) -> Unit, onBack: () -> Unit) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 22.dp),
    ) {
        item { Text("Sort", textAlign = TextAlign.Center) }
        items(SortMode.entries.size) { index ->
            val mode = SortMode.entries[index]
            Chip(
                label = { Text(if (mode == current) "✓ ${mode.label}" else mode.label) },
                onClick = { onSelect(mode) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        item { Button(onClick = onBack) { Text("Back") } }
    }
}

@Composable
fun SearchScreen(query: String, resultsCount: Int, onPreset: (String) -> Unit, onBack: () -> Unit) {
    val presets = listOf("jpg", "mp3", "zip", "download", "backup")
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 22.dp),
    ) {
        item { Text("Search", textAlign = TextAlign.Center) }
        item { Text(if (query.isBlank()) "Choose quick search" else "$resultsCount results for $query", textAlign = TextAlign.Center) }
        items(presets.size) { index ->
            Chip(label = { Text(presets[index]) }, onClick = { onPreset(presets[index]) }, modifier = Modifier.fillMaxWidth())
        }
        item { Button(onClick = onBack) { Text("Back") } }
    }
}

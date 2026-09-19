package com.sere.filemanager.wear

import androidx.compose.runtime.Composable
import com.sere.filemanager.core.files.SortMode
import com.sere.filemanager.wear.ui.WearRotaryList
import com.sere.filemanager.wear.ui.wearBackAction
import com.sere.filemanager.wear.ui.wearPrimaryAction
import com.sere.filemanager.wear.ui.wearTitle

@Composable
fun SortScreen(current: SortMode, onSelect: (SortMode) -> Unit, onBack: () -> Unit) {
    WearRotaryList {
        wearTitle("Sort")
        SortMode.entries.forEach { mode ->
            wearPrimaryAction(if (mode == current) "✓ ${mode.label}" else mode.label) { onSelect(mode) }
        }
        wearBackAction(onBack)
    }
}

@Composable
fun SearchScreen(query: String, resultsCount: Int, onPreset: (String) -> Unit, onBack: () -> Unit) {
    val presets = listOf("jpg", "mp3", "zip", "download", "backup")
    WearRotaryList {
        wearTitle("Search", if (query.isBlank()) "Choose quick search" else "$resultsCount results for $query")
        presets.forEach { preset ->
            wearPrimaryAction(preset) { onPreset(preset) }
        }
        wearBackAction(onBack)
    }
}

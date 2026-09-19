package com.sere.filemanager.wear

import androidx.compose.runtime.Composable
import com.sere.filemanager.wear.ui.WearRotaryList
import com.sere.filemanager.wear.ui.wearBackAction
import com.sere.filemanager.wear.ui.wearInfo
import com.sere.filemanager.wear.ui.wearPrimaryAction
import com.sere.filemanager.wear.ui.wearTitle

@Composable
fun WearClipboardBanner(
    state: WearClipboardState,
    onPaste: () -> Unit,
    onClear: () -> Unit,
) {
    if (!state.hasEntry) return
    WearRotaryList {
        wearTitle("Clipboard")
        wearInfo("${state.mode}: ${state.fileName}")
        wearPrimaryAction("Paste here", onPaste)
        wearPrimaryAction("Clear", onClear)
    }
}

@Composable
fun WearOperationHelpScreen(onBack: () -> Unit) {
    WearRotaryList {
        wearTitle("File actions")
        wearInfo("Tap folder to open. Tap file to open actions. Use Copy/Move then Paste in destination.")
        wearBackAction(onBack)
    }
}

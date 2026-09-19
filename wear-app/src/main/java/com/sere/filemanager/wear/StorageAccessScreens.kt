package com.sere.filemanager.wear

import androidx.compose.runtime.Composable
import com.sere.filemanager.wear.ui.WearRotaryList
import com.sere.filemanager.wear.ui.wearBackAction
import com.sere.filemanager.wear.ui.wearInfo
import com.sere.filemanager.wear.ui.wearPrimaryAction
import com.sere.filemanager.wear.ui.wearTitle

@Composable
fun StorageAccessScreen(onMedia: () -> Unit, onAdvanced: () -> Unit, onBack: () -> Unit) {
    WearRotaryList {
        wearTitle("Storage access")
        wearInfo("Use media permissions for normal files. Use ADB guide for protected locations.")
        wearPrimaryAction("Media access", onMedia)
        wearPrimaryAction("ADB guide", onAdvanced)
        wearBackAction(onBack)
    }
}

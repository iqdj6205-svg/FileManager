package com.sere.filemanager.wear

import androidx.compose.runtime.Composable
import com.sere.filemanager.wear.ui.WearRotaryList
import com.sere.filemanager.wear.ui.wearBackAction
import com.sere.filemanager.wear.ui.wearInfo
import com.sere.filemanager.wear.ui.wearPrimaryAction
import com.sere.filemanager.wear.ui.wearTitle

@Composable
fun PermissionScreen(onRequestMedia: () -> Unit, onBack: () -> Unit) {
    WearRotaryList {
        wearTitle("Permissions")
        wearInfo("Grant media access to browse images, videos, and audio.")
        wearPrimaryAction("Grant media", onRequestMedia)
        wearInfo("Full system memory requires ADB/root workflows and is not normal app access.")
        wearBackAction(onBack)
    }
}

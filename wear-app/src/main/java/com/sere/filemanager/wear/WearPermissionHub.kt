package com.sere.filemanager.wear

import androidx.compose.runtime.Composable
import com.sere.filemanager.wear.ui.WearRotaryList
import com.sere.filemanager.wear.ui.wearBackAction
import com.sere.filemanager.wear.ui.wearInfo
import com.sere.filemanager.wear.ui.wearPrimaryAction
import com.sere.filemanager.wear.ui.wearSecondaryAction
import com.sere.filemanager.wear.ui.wearTitle

@Composable
fun WearPermissionHub(
    state: WearPermissionHubState,
    onGrantMedia: () -> Unit,
    onGrantStorage: () -> Unit,
    onGrantNotifications: () -> Unit,
    onAdbGuide: () -> Unit,
    onBack: () -> Unit,
) {
    WearRotaryList {
        wearTitle("Permissions", "Missing: ${state.missingCount}")
        if (state.missingCount == 0) wearInfo("All basic permissions granted")
        if (!state.storageGranted) wearPrimaryAction("Grant files", onGrantStorage)
        if (!state.mediaGranted) wearPrimaryAction("Grant media", onGrantMedia)
        if (!state.notificationsGranted) wearSecondaryAction("Grant notifications", onGrantNotifications)
        wearSecondaryAction("ADB advanced", onAdbGuide)
        wearBackAction(onBack)
    }
}

@Composable
fun WearAdbGuideScreen(onBack: () -> Unit) {
    WearRotaryList {
        wearTitle("ADB access")
        AdbAccessGuide.commands.forEach { command -> wearInfo(command) }
        AdbAccessGuide.notes.forEach { note -> wearInfo("• $note") }
        wearBackAction(onBack)
    }
}

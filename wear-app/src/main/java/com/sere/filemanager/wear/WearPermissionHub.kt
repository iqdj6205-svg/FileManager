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
        wearInfo(state.storageLabel)
        wearInfo(state.mediaLabel)
        wearInfo("Media permission lets FileManager show photos, videos, and audio visible to Android.")
        wearInfo("It does not unlock protected system folders.")
        wearInfo("Use ADB advanced only if you understand the risk and need broader storage access.")
        if (state.missingCount == 0) wearInfo("Core access is ready")
        if (!state.storageGranted) wearPrimaryAction("Grant files", onGrantStorage)
        if (!state.mediaGranted) wearSecondaryAction("Grant media", onGrantMedia)
        if (!state.notificationsGranted) wearSecondaryAction("Grant notifications", onGrantNotifications)
        wearSecondaryAction("ADB advanced", onAdbGuide)
        wearBackAction(onBack)
    }
}

@Composable
fun WearAdbGuideScreen(onBack: () -> Unit) {
    WearRotaryList {
        wearTitle("ADB access")
        wearInfo("Advanced ADB access is optional. The watch app should work for normal media/files without it.")
        wearInfo("Only use these commands for protected paths or power-user testing.")
        AdbAccessGuide.commands.forEach { command -> wearInfo(command) }
        AdbAccessGuide.notes.forEach { note -> wearInfo("• $note") }
        wearBackAction(onBack)
    }
}

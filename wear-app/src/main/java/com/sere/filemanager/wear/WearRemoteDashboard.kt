package com.sere.filemanager.wear

import androidx.compose.runtime.Composable
import com.sere.filemanager.core.model.RemoteSession
import com.sere.filemanager.core.remote.RemoteServerStatusStore
import com.sere.filemanager.wear.ui.WearRotaryList
import com.sere.filemanager.wear.ui.wearBackAction
import com.sere.filemanager.wear.ui.wearInfo
import com.sere.filemanager.wear.ui.wearPrimaryAction
import com.sere.filemanager.wear.ui.wearSecondaryAction
import com.sere.filemanager.wear.ui.wearTitle

@Composable
fun WearRemoteDashboard(
    session: RemoteSession = RemoteServerStatusStore.current(),
    networkLabel: String,
    batteryPercent: Int?,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onSettings: () -> Unit,
    onBack: () -> Unit,
) {
    val subtitle = if (session.url == null) "Server stopped" else "Server running"
    WearRotaryList {
        wearTitle("Remote", subtitle)
        wearInfo("State: ${session.state}")
        wearInfo("Network: $networkLabel")
        batteryPercent?.let { wearInfo("Battery: $it%") }
        session.url?.let { wearInfo("URL: $it") }
        session.pin?.let { wearInfo("PIN: $it") }
        if (session.url == null) wearInfo("Connect watch to network and start server.")
        if (session.url == null) wearPrimaryAction("Start", onStart) else wearPrimaryAction("Stop", onStop)
        wearSecondaryAction("Settings", onSettings)
        wearBackAction(onBack)
    }
}

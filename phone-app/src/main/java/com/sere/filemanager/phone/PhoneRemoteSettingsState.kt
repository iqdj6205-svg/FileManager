package com.sere.filemanager.phone

import androidx.compose.runtime.Immutable
import com.sere.filemanager.core.model.RemoteServerSettings

@Immutable
data class PhoneRemoteSettingsState(
    val port: Int = 8080,
    val requirePin: Boolean = true,
    val allowUploads: Boolean = false,
    val allowDelete: Boolean = false,
    val autoStopMinutes: Int = 15,
    val localNetworkOnly: Boolean = true,
    val advancedMode: Boolean = false,
    val showHiddenFiles: Boolean = false,
) {
    companion object {
        fun from(remote: RemoteServerSettings, advancedMode: Boolean, showHiddenFiles: Boolean) = PhoneRemoteSettingsState(
            port = remote.port,
            requirePin = remote.requirePin,
            allowUploads = remote.allowUploads,
            allowDelete = remote.allowDelete,
            autoStopMinutes = remote.autoStopMinutes,
            localNetworkOnly = remote.localNetworkOnly,
            advancedMode = advancedMode,
            showHiddenFiles = showHiddenFiles,
        )
    }
}

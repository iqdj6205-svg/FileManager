package com.sere.filemanager.phone

import androidx.compose.runtime.Immutable

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
)

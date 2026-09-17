package com.sere.filemanager.phone

import androidx.compose.runtime.Immutable
import com.sere.filemanager.core.files.StorageRootState
import com.sere.filemanager.core.media.MediaPlaybackSession
import com.sere.filemanager.core.model.AppSettings
import com.sere.filemanager.core.model.CompanionState

@Immutable
data class PhoneAppState(
    val companion: CompanionState = CompanionState(),
    val browser: PhoneFileBrowserState = PhoneFileBrowserState(),
    val storageRoots: StorageRootState = StorageRootState(),
    val analyzer: PhoneAnalyzerState = PhoneAnalyzerState(),
    val media: PhoneMediaState = PhoneMediaState(),
    val playback: MediaPlaybackSession = MediaPlaybackSession(),
    val appSettings: AppSettings = AppSettings(),
    val remoteSettings: PhoneRemoteSettingsState = PhoneRemoteSettingsState(),
    val transfer: PhoneTransferState = PhoneTransferState(),
    val fileActions: PhoneFileActionState = PhoneFileActionState(),
    val edit: PhoneFileEditState = PhoneFileEditState(),
    val remoteUrl: String = "",
    val statusMessage: String = "Phone file manager ready",
)

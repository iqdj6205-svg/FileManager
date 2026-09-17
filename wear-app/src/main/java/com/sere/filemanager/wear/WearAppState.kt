package com.sere.filemanager.wear

import androidx.compose.runtime.Immutable
import com.sere.filemanager.core.media.ImagePreviewState
import com.sere.filemanager.core.media.MediaPlaybackSession
import com.sere.filemanager.core.remote.RemoteServerSession

@Immutable
data class WearAppState(
    val browser: BrowserState = BrowserState(),
    val permissions: PermissionRequestState = PermissionRequestState(),
    val operation: OperationUiState = OperationUiState(),
    val wearOperations: WearOperationState = WearOperationState(),
    val media: WearMediaState = WearMediaState(),
    val playback: MediaPlaybackSession = MediaPlaybackSession(),
    val imagePreview: ImagePreviewState = ImagePreviewState(),
    val remoteSession: RemoteServerSession = RemoteServerSession.stopped(),
    val advancedModeEnabled: Boolean = false,
    val batterySaverEnabled: Boolean = true,
)

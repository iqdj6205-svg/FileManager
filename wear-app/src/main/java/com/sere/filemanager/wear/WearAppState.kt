package com.sere.filemanager.wear

import androidx.compose.runtime.Immutable
import com.sere.filemanager.core.remote.RemoteServerSession

@Immutable
data class WearAppState(
    val browser: BrowserState = BrowserState(),
    val permissions: PermissionRequestState = PermissionRequestState(),
    val operation: OperationUiState = OperationUiState(),
    val wearOperations: WearOperationState = WearOperationState(),
    val remoteSession: RemoteServerSession = RemoteServerSession.stopped(),
    val advancedModeEnabled: Boolean = false,
    val batterySaverEnabled: Boolean = true,
)

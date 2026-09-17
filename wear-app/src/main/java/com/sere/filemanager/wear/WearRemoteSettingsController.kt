package com.sere.filemanager.wear

import com.sere.filemanager.core.wearbridge.WearBridgeSettingsPayload

class WearRemoteSettingsController {
    fun currentUiState(): WearRemoteSettingsUiState {
        val current = WearSettingsStore.current()
        return WearRemoteSettingsUiState(
            port = current.remote.port,
            requirePin = current.remote.requirePin,
            allowUploads = current.remote.allowUploads,
            allowDelete = current.remote.allowDelete,
            autoStopMinutes = current.remote.autoStopMinutes,
        )
    }

    fun update(transform: (WearBridgeSettingsPayload) -> WearBridgeSettingsPayload): WearRemoteSettingsUiState {
        WearSettingsStore.update(transform(WearSettingsStore.current()))
        return currentUiState()
    }

    fun toggleUploads(): WearRemoteSettingsUiState = update { it.copy(remote = it.remote.copy(allowUploads = !it.remote.allowUploads)) }
    fun toggleDelete(): WearRemoteSettingsUiState = update { it.copy(remote = it.remote.copy(allowDelete = !it.remote.allowDelete)) }
    fun togglePin(): WearRemoteSettingsUiState = update { it.copy(remote = it.remote.copy(requirePin = !it.remote.requirePin)) }
}

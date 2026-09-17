package com.sere.filemanager.core.wearbridge

import com.sere.filemanager.core.model.RemoteServerSettings

enum class WearBridgeCommandType {
    StartWatchServer,
    StopWatchServer,
    GetWatchServerStatus,
    SyncSettings,
    GetStorageStatus,
    PrepareFileTransfer,
    SendFileToWatch,
    RequestFileFromWatch,
}

data class WearBridgeCommand(
    val id: String,
    val type: WearBridgeCommandType,
    val payload: String? = null,
    val createdAtMillis: Long = System.currentTimeMillis(),
)

data class WearBridgeCommandResult(
    val commandId: String,
    val success: Boolean,
    val message: String? = null,
    val payload: String? = null,
)

data class WearBridgeRemoteStatus(
    val running: Boolean,
    val url: String? = null,
    val pin: String? = null,
    val networkLabel: String? = null,
    val batteryPercent: Int? = null,
)

data class WearBridgeSettingsPayload(
    val remote: RemoteServerSettings = RemoteServerSettings(),
    val advancedMode: Boolean = false,
    val showHiddenFiles: Boolean = false,
)

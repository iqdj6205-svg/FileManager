package com.sere.filemanager.core.wearbridge

import com.sere.filemanager.core.model.RemoteServerSettings

object WearBridgeSettingsStringCodec {
    fun encode(payload: WearBridgeSettingsPayload): String = listOf(
        payload.remote.port,
        payload.remote.requirePin,
        payload.remote.allowUploads,
        payload.remote.allowDelete,
        payload.remote.autoStopMinutes,
        payload.remote.localNetworkOnly,
        payload.advancedMode,
        payload.showHiddenFiles,
    ).joinToString("|")

    fun decode(value: String?): WearBridgeSettingsPayload? {
        if (value.isNullOrBlank()) return null
        val p = value.split('|')
        return WearBridgeSettingsPayload(
            remote = RemoteServerSettings(
                port = p.getOrNull(0)?.toIntOrNull() ?: 8080,
                requirePin = p.getOrNull(1)?.toBooleanStrictOrNull() ?: true,
                allowUploads = p.getOrNull(2)?.toBooleanStrictOrNull() ?: false,
                allowDelete = p.getOrNull(3)?.toBooleanStrictOrNull() ?: false,
                autoStopMinutes = p.getOrNull(4)?.toIntOrNull() ?: 15,
                localNetworkOnly = p.getOrNull(5)?.toBooleanStrictOrNull() ?: true,
            ),
            advancedMode = p.getOrNull(6)?.toBooleanStrictOrNull() ?: false,
            showHiddenFiles = p.getOrNull(7)?.toBooleanStrictOrNull() ?: false,
        )
    }
}

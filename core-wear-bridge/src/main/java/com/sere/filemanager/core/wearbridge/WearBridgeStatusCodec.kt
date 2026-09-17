package com.sere.filemanager.core.wearbridge

import com.google.android.gms.wearable.DataMap
import com.sere.filemanager.core.model.RemoteServerSettings

object WearBridgeStatusCodec {
    fun remoteStatusToDataMap(status: WearBridgeRemoteStatus): DataMap = DataMap().apply {
        putBoolean(WearBridgeDataKeys.RUNNING, status.running)
        putString(WearBridgeDataKeys.URL, status.url.orEmpty())
        putString(WearBridgeDataKeys.PIN, status.pin.orEmpty())
        putString(WearBridgeDataKeys.NETWORK, status.networkLabel.orEmpty())
        status.batteryPercent?.let { putInt(WearBridgeDataKeys.BATTERY, it) }
    }

    fun remoteStatusFromDataMap(map: DataMap): WearBridgeRemoteStatus = WearBridgeRemoteStatus(
        running = map.getBoolean(WearBridgeDataKeys.RUNNING),
        url = map.getString(WearBridgeDataKeys.URL)?.takeIf { it.isNotBlank() },
        pin = map.getString(WearBridgeDataKeys.PIN)?.takeIf { it.isNotBlank() },
        networkLabel = map.getString(WearBridgeDataKeys.NETWORK)?.takeIf { it.isNotBlank() },
        batteryPercent = if (map.containsKey(WearBridgeDataKeys.BATTERY)) map.getInt(WearBridgeDataKeys.BATTERY) else null,
    )

    fun settingsToDataMap(settings: WearBridgeSettingsPayload): DataMap = DataMap().apply {
        putInt(WearBridgeDataKeys.PORT, settings.remote.port)
        putBoolean(WearBridgeDataKeys.REQUIRE_PIN, settings.remote.requirePin)
        putBoolean(WearBridgeDataKeys.ALLOW_UPLOADS, settings.remote.allowUploads)
        putBoolean(WearBridgeDataKeys.ALLOW_DELETE, settings.remote.allowDelete)
        putInt(WearBridgeDataKeys.AUTO_STOP_MINUTES, settings.remote.autoStopMinutes)
        putBoolean(WearBridgeDataKeys.LOCAL_NETWORK_ONLY, settings.remote.localNetworkOnly)
        putBoolean(WearBridgeDataKeys.ADVANCED_MODE, settings.advancedMode)
        putBoolean(WearBridgeDataKeys.SHOW_HIDDEN, settings.showHiddenFiles)
    }

    fun settingsFromDataMap(map: DataMap): WearBridgeSettingsPayload = WearBridgeSettingsPayload(
        remote = RemoteServerSettings(
            port = map.getInt(WearBridgeDataKeys.PORT, 8080),
            requirePin = map.getBoolean(WearBridgeDataKeys.REQUIRE_PIN, true),
            allowUploads = map.getBoolean(WearBridgeDataKeys.ALLOW_UPLOADS, false),
            allowDelete = map.getBoolean(WearBridgeDataKeys.ALLOW_DELETE, false),
            autoStopMinutes = map.getInt(WearBridgeDataKeys.AUTO_STOP_MINUTES, 15),
            localNetworkOnly = map.getBoolean(WearBridgeDataKeys.LOCAL_NETWORK_ONLY, true),
        ),
        advancedMode = map.getBoolean(WearBridgeDataKeys.ADVANCED_MODE, false),
        showHiddenFiles = map.getBoolean(WearBridgeDataKeys.SHOW_HIDDEN, false),
    )
}

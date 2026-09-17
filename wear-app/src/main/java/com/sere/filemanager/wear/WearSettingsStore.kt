package com.sere.filemanager.wear

import com.sere.filemanager.core.wearbridge.WearBridgeSettingsPayload

object WearSettingsStore {
    @Volatile private var settings: WearBridgeSettingsPayload = WearBridgeSettingsPayload()

    fun update(newSettings: WearBridgeSettingsPayload) { settings = newSettings }
    fun current(): WearBridgeSettingsPayload = settings
}

package com.sere.filemanager.phone

import com.sere.filemanager.core.wearbridge.WearBridgeCommandResult

object PhoneBridgeStatusStore {
    @Volatile private var latestResult: WearBridgeCommandResult? = null

    fun update(result: WearBridgeCommandResult) { latestResult = result }
    fun latest(): WearBridgeCommandResult? = latestResult
    fun clear() { latestResult = null }
}

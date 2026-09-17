package com.sere.filemanager.phone

import com.sere.filemanager.core.wearbridge.WearBridgeRemoteStatus

object PhoneRemoteStatusStore {
    @Volatile private var latest: WearBridgeRemoteStatus? = null
    fun update(status: WearBridgeRemoteStatus) { latest = status }
    fun latest(): WearBridgeRemoteStatus? = latest
    fun clear() { latest = null }
}

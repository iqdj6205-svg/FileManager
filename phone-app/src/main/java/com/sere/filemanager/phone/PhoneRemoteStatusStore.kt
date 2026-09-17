package com.sere.filemanager.phone

import com.sere.filemanager.core.wearbridge.WearBridgeRemoteStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object PhoneRemoteStatusStore {
    private val _status = MutableStateFlow<WearBridgeRemoteStatus?>(null)
    val status: StateFlow<WearBridgeRemoteStatus?> = _status

    @Volatile private var latest: WearBridgeRemoteStatus? = null

    fun update(snapshot: WearBridgeRemoteStatus) {
        latest = snapshot
        _status.value = snapshot
    }

    fun latest(): WearBridgeRemoteStatus? = latest

    fun clear() {
        latest = null
        _status.value = null
    }
}

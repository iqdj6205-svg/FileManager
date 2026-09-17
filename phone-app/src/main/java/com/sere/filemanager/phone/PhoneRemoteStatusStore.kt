package com.sere.filemanager.phone

import com.sere.filemanager.core.wearbridge.WearRemoteStatusSnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object PhoneRemoteStatusStore {
    private val _status = MutableStateFlow<WearRemoteStatusSnapshot?>(null)
    val status: StateFlow<WearRemoteStatusSnapshot?> = _status

    @Volatile private var latest: WearRemoteStatusSnapshot? = null

    fun update(snapshot: WearRemoteStatusSnapshot) {
        latest = snapshot
        _status.value = snapshot
    }

    fun latest(): WearRemoteStatusSnapshot? = latest

    fun clear() {
        latest = null
        _status.value = null
    }
}

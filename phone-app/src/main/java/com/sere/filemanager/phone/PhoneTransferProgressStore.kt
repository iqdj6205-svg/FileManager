package com.sere.filemanager.phone

import com.sere.filemanager.core.wearbridge.WearFileTransferProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object PhoneTransferProgressStore {
    private val _progress = MutableStateFlow<WearFileTransferProgress?>(null)
    val progress: StateFlow<WearFileTransferProgress?> = _progress

    @Volatile private var latest: WearFileTransferProgress? = null

    fun update(progress: WearFileTransferProgress) {
        latest = progress
        _progress.value = progress
    }

    fun latest(): WearFileTransferProgress? = latest

    fun clear() {
        latest = null
        _progress.value = null
    }
}

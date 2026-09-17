package com.sere.filemanager.wear

import com.sere.filemanager.core.wearbridge.WearFileTransferRequest

object WearPendingTransferStore {
    @Volatile private var latest: WearFileTransferRequest? = null

    fun update(request: WearFileTransferRequest) { latest = request }
    fun consume(): WearFileTransferRequest? = latest.also { latest = null }
    fun peek(): WearFileTransferRequest? = latest
}

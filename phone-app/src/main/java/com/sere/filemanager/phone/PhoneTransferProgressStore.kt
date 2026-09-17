package com.sere.filemanager.phone

import com.sere.filemanager.core.wearbridge.WearFileTransferProgress

object PhoneTransferProgressStore {
    @Volatile private var latest: WearFileTransferProgress? = null
    fun update(progress: WearFileTransferProgress) { latest = progress }
    fun latest(): WearFileTransferProgress? = latest
    fun clear() { latest = null }
}

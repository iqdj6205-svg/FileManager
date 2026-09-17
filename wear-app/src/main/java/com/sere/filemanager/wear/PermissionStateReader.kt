package com.sere.filemanager.wear

import android.content.Context
import com.sere.filemanager.core.model.AppPermissionState

class PermissionStateReader(context: Context) {
    private val manager = WearPermissionManager(context)

    fun hasMediaAccess(): Boolean {
        val status = manager.status()
        return listOf(status.mediaImages, status.mediaVideo, status.mediaAudio).any { it == AppPermissionState.Granted }
    }
}

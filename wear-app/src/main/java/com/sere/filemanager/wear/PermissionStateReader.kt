package com.sere.filemanager.wear

import android.content.Context
import com.sere.filemanager.core.model.AppPermissionState
import com.sere.filemanager.core.model.PermissionStatus

class PermissionStateReader(context: Context) {
    private val manager = WearPermissionManager(context)

    fun status(): PermissionStatus = manager.status()

    fun hasAnyMediaAccess(): Boolean {
        val status = status()
        return listOf(status.mediaImages, status.mediaVideo, status.mediaAudio).any { it == AppPermissionState.Granted }
    }

    fun hasFullMediaAccess(): Boolean {
        val status = status()
        return listOf(status.mediaImages, status.mediaVideo, status.mediaAudio).all { it == AppPermissionState.Granted }
    }
}

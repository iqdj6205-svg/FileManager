package com.sere.filemanager.wear

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.sere.filemanager.core.model.AppPermissionState
import com.sere.filemanager.core.model.PermissionStatus

class WearPermissionManager(private val context: Context) {
    fun status(): PermissionStatus {
        fun state(permission: String): AppPermissionState = if (
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        ) AppPermissionState.Granted else AppPermissionState.Denied

        val media = PermissionHelper.mediaPermissions()
        return PermissionStatus(
            mediaImages = media.getOrNull(0)?.let(::state) ?: AppPermissionState.Granted,
            mediaVideo = media.getOrNull(1)?.let(::state) ?: AppPermissionState.Granted,
            mediaAudio = media.getOrNull(2)?.let(::state) ?: AppPermissionState.Granted,
            notifications = PermissionHelper.notificationPermissions().firstOrNull()?.let(::state) ?: AppPermissionState.Granted,
        )
    }
}

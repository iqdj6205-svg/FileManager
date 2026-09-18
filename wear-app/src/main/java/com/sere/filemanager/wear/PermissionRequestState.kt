package com.sere.filemanager.wear

import androidx.compose.runtime.Immutable
import com.sere.filemanager.core.model.AppPermissionState
import com.sere.filemanager.core.model.PermissionStatus

@Immutable
data class PermissionRequestState(
    val status: PermissionStatus = PermissionStatus(),
    val lastRequestMessage: String? = null,
) {
    val mediaGranted: Boolean
        get() = listOf(status.mediaImages, status.mediaVideo, status.mediaAudio)
            .all { it == AppPermissionState.Granted }

    val storageGranted: Boolean
        get() = status.mediaImages == AppPermissionState.Granted ||
            status.mediaVideo == AppPermissionState.Granted ||
            status.mediaAudio == AppPermissionState.Granted

    val notificationsGranted: Boolean get() = status.notifications == AppPermissionState.Granted
}

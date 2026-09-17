package com.sere.filemanager.wear

import androidx.compose.runtime.Immutable

@Immutable
data class WearPermissionHubState(
    val mediaGranted: Boolean = false,
    val storageGranted: Boolean = false,
    val notificationsGranted: Boolean = false,
) {
    val allBasicGranted: Boolean get() = mediaGranted && storageGranted
    val missingCount: Int get() = listOf(mediaGranted, storageGranted, notificationsGranted).count { !it }
}

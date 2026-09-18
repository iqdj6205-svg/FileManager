package com.sere.filemanager.wear

import androidx.compose.runtime.Immutable

@Immutable
data class WearPermissionHubState(
    val mediaGranted: Boolean = false,
    val storageGranted: Boolean = false,
    val notificationsGranted: Boolean = false,
) {
    val allBasicGranted: Boolean get() = storageGranted
    val missingCount: Int get() = listOf(storageGranted, notificationsGranted).count { !it }
    val mediaLabel: String get() = if (mediaGranted) "Media granted" else "Media partially/missing"
    val storageLabel: String get() = if (storageGranted) "Files visible" else "Files blocked"
}

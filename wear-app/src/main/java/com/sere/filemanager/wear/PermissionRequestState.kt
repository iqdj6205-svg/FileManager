package com.sere.filemanager.wear

import androidx.compose.runtime.Immutable
import com.sere.filemanager.core.model.PermissionStatus

@Immutable
data class PermissionRequestState(
    val status: PermissionStatus = PermissionStatus(),
    val lastRequestMessage: String? = null,
)

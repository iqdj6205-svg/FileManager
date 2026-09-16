package com.sere.filemanager.core.model

enum class AppPermissionState { Unknown, Granted, Denied, PermanentlyDenied }

data class PermissionStatus(
    val mediaImages: AppPermissionState = AppPermissionState.Unknown,
    val mediaVideo: AppPermissionState = AppPermissionState.Unknown,
    val mediaAudio: AppPermissionState = AppPermissionState.Unknown,
    val notifications: AppPermissionState = AppPermissionState.Unknown,
)

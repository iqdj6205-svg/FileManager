package com.sere.filemanager.core.remote

data class RemoteFeatureFlags(
    val enableDownload: Boolean = true,
    val enableUpload: Boolean = false,
    val enableRename: Boolean = false,
    val enableDelete: Boolean = false,
    val enableMkdir: Boolean = false,
)

fun RemoteConfig.toFeatureFlags(): RemoteFeatureFlags = RemoteFeatureFlags(
    enableDownload = true,
    enableUpload = allowUploads,
    enableRename = allowDelete,
    enableDelete = allowDelete,
    enableMkdir = allowUploads,
)

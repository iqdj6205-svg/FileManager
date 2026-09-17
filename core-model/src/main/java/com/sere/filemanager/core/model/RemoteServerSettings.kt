package com.sere.filemanager.core.model

data class RemoteServerSettings(
    val port: Int = 8080,
    val requirePin: Boolean = true,
    val allowUploads: Boolean = false,
    val allowDelete: Boolean = false,
    val autoStopMinutes: Int = 15,
    val localNetworkOnly: Boolean = true,
)

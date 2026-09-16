package com.sere.filemanager.core.model

data class StorageVolume(
    val id: String,
    val label: String,
    val path: String,
    val totalBytes: Long? = null,
    val freeBytes: Long? = null,
)

data class OperationProgress(
    val title: String,
    val completedBytes: Long = 0,
    val totalBytes: Long? = null,
    val isCancellable: Boolean = true,
)

data class BatteryPolicy(
    val stopRemoteServerBelowPercent: Int = 20,
    val thumbnailGenerationBelowPercent: Int = 25,
    val autoStopServerMinutes: Int = 15,
)

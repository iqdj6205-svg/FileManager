package com.sere.filemanager.core.remote

data class DownloadRequest(
    val path: String,
    val pin: String?,
)

data class DownloadPlan(
    val path: String,
    val fileName: String,
    val mimeType: String = "application/octet-stream",
    val sizeBytes: Long? = null,
)

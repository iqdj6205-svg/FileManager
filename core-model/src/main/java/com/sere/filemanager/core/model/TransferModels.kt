package com.sere.filemanager.core.model

enum class TransferDirection { PhoneToWatch, WatchToPhone }
enum class TransferState { Queued, Running, Completed, Failed, Cancelled }

data class FileTransfer(
    val id: String,
    val fileName: String,
    val sourcePath: String,
    val targetPath: String,
    val direction: TransferDirection,
    val state: TransferState = TransferState.Queued,
    val progressBytes: Long = 0,
    val totalBytes: Long? = null,
    val error: String? = null,
)

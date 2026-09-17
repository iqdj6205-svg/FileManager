package com.sere.filemanager.core.wearbridge

import java.util.UUID

enum class WearTransferDirection { PhoneToWatch, WatchToPhone }
enum class WearTransferState { Preparing, OpeningChannel, Transferring, Completed, Failed, Cancelled }

data class WearFileTransferRequest(
    val id: String = UUID.randomUUID().toString(),
    val fileName: String,
    val sourcePath: String? = null,
    val targetPath: String,
    val direction: WearTransferDirection,
    val sizeBytes: Long? = null,
)

data class WearFileTransferProgress(
    val requestId: String,
    val state: WearTransferState,
    val transferredBytes: Long = 0,
    val totalBytes: Long? = null,
    val message: String? = null,
)

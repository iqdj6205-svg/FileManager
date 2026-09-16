package com.sere.filemanager.wear

import com.sere.filemanager.core.files.FileOperation

data class OperationUiState(
    val pendingOperation: FileOperation? = null,
    val message: String? = null,
    val inProgress: Boolean = false,
)

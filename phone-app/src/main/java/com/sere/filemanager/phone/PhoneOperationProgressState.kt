package com.sere.filemanager.phone

import androidx.compose.runtime.Immutable
import com.sere.filemanager.core.files.FileOperationProgress

@Immutable
data class PhoneOperationProgressState(
    val current: FileOperationProgress = FileOperationProgress(),
) {
    val visible: Boolean get() = current.operationLabel != "Idle" || current.message != null
}

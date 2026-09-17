package com.sere.filemanager.phone

import androidx.compose.runtime.Immutable
import com.sere.filemanager.core.wearbridge.WearFileTransferProgress

@Immutable
data class PhoneTransferState(
    val selectedFileName: String? = null,
    val targetPath: String = "/sdcard/Download",
    val latestProgress: WearFileTransferProgress? = null,
    val message: String? = null,
)

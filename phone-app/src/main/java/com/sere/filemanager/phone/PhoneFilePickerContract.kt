package com.sere.filemanager.phone

import android.net.Uri

data class PickedFile(
    val uri: Uri,
    val displayName: String = "selected-file.bin",
    val sizeBytes: Long? = null,
)

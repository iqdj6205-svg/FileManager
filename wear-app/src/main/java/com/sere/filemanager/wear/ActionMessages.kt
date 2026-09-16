package com.sere.filemanager.wear

import com.sere.filemanager.core.files.FileOperationResult

object ActionMessages {
    fun from(result: FileOperationResult): String = if (result.success) {
        result.message ?: "Done"
    } else {
        result.message ?: "Failed"
    }
}

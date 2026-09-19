package com.sere.filemanager.core.ui

object UiFormatters {
    fun compactBytes(bytes: Long?): String {
        if (bytes == null) return ""
        return com.sere.filemanager.core.files.StorageFormatter.bytes(bytes).replace(" ", "").replace("—", "")
    }
}

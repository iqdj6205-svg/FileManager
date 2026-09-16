package com.sere.filemanager.core.ui

object UiFormatters {
    fun compactBytes(bytes: Long?): String {
        if (bytes == null) return ""
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        var value = bytes.toDouble()
        var unitIndex = 0
        while (value >= 1024 && unitIndex < units.lastIndex) {
            value /= 1024
            unitIndex++
        }
        return if (unitIndex == 0) "${bytes}B" else "%.1f%s".format(value, units[unitIndex])
    }
}

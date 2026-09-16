package com.sere.filemanager.core.files

object StorageFormatter {
    fun bytes(bytes: Long?): String {
        if (bytes == null) return "—"
        val units = listOf("B", "KB", "MB", "GB", "TB")
        var value = bytes.toDouble()
        var unit = 0
        while (value >= 1024 && unit < units.lastIndex) {
            value /= 1024
            unit++
        }
        return if (unit == 0) "${bytes} ${units[unit]}" else "%.1f %s".format(value, units[unit])
    }
}

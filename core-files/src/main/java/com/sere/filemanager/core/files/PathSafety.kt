package com.sere.filemanager.core.files

object PathSafety {
    private val blockedPrefixes = listOf("/system", "/proc", "/dev", "/sys")
    private val blockedSegments = setOf("..", "~")

    fun isProbablySafeForNormalMode(path: String): Boolean {
        val clean = path.trim()
        if (clean.isBlank()) return false
        if (clean.split('/').any { it in blockedSegments }) return false
        return blockedPrefixes.none { clean == it || clean.startsWith("$it/") }
    }

    fun explainIfBlocked(path: String): String? {
        val clean = path.trim()
        if (clean.isBlank()) return "Path is empty."
        if (clean.split('/').any { it in blockedSegments }) return "Path traversal is not allowed."
        if (blockedPrefixes.any { clean == it || clean.startsWith("$it/") }) return "This path is protected. Use ADB/root workflows only if you understand the risk."
        return null
    }
}

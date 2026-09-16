package com.sere.filemanager.core.remote

object RemotePathGuard {
    private val deniedSegments = setOf("..", "~")
    private val deniedPrefixes = listOf("/system", "/proc", "/dev", "/sys")

    fun validate(path: String): String? {
        val clean = path.trim()
        if (clean.isBlank()) return "Path is empty"
        if (clean.split('/').any { it in deniedSegments }) return "Path traversal is not allowed"
        if (deniedPrefixes.any { clean == it || clean.startsWith("$it/") }) return "Protected system path is blocked"
        return null
    }
}

package com.sere.filemanager.core.files

object PathSafety {
    private val blockedPrefixes = listOf("/system", "/proc", "/dev", "/sys")

    fun isProbablySafeForNormalMode(path: String): Boolean {
        val clean = path.trim()
        return clean.isNotBlank() && blockedPrefixes.none { clean == it || clean.startsWith("$it/") }
    }

    fun explainIfBlocked(path: String): String? = if (isProbablySafeForNormalMode(path)) null else
        "This path is protected. Use ADB/root workflows only if you understand the risk."
}

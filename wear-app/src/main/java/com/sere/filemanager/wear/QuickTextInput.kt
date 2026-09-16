package com.sere.filemanager.wear

/**
 * Wear OS text entry is intentionally isolated. Real builds should use RemoteInput,
 * voice input, or phone companion entry instead of forcing tiny keyboard usage.
 */
object QuickTextInputPresets {
    val folderNames = listOf("New folder", "Downloads", "Media", "Backup")
    fun copyName(original: String): String = "copy-$original"
}

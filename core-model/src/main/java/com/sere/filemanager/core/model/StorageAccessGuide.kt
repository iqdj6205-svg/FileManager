package com.sere.filemanager.core.model

data class StorageAccessGuide(
    val title: String,
    val description: String,
    val steps: List<String>,
)

object DefaultStorageAccessGuides {
    val mediaAccess = StorageAccessGuide(
        title = "Media access",
        description = "Allows the app to display images, video, and audio files supported by Android permissions.",
        steps = listOf("Open Settings", "Grant media permissions", "Return to FileManager"),
    )

    val adbAdvanced = StorageAccessGuide(
        title = "ADB advanced access",
        description = "For power users. Android may still protect system folders unless device policy/root allows access.",
        steps = listOf("Enable developer options", "Enable ADB debugging", "Connect from PC", "Use adb push/pull for protected workflows"),
    )
}

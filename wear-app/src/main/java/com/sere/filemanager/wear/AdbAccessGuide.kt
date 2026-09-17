package com.sere.filemanager.wear

object AdbAccessGuide {
    val commands = listOf(
        "adb devices",
        "adb shell appops set com.sere.filemanager.wear MANAGE_EXTERNAL_STORAGE allow",
        "adb shell pm grant com.sere.filemanager.wear android.permission.READ_EXTERNAL_STORAGE",
        "adb shell pm grant com.sere.filemanager.wear android.permission.READ_MEDIA_IMAGES",
        "adb shell pm grant com.sere.filemanager.wear android.permission.READ_MEDIA_VIDEO",
        "adb shell pm grant com.sere.filemanager.wear android.permission.READ_MEDIA_AUDIO",
        "adb shell am force-stop com.sere.filemanager.wear"
    )

    val notes = listOf(
        "Use only for your own device and only when you understand Android storage permissions.",
        "MANAGE_EXTERNAL_STORAGE can be restricted by device policy, OEM firmware, or Play policy.",
        "On Android 13+, media permissions are split into images, video, and audio.",
        "Restart the app after changing permissions from ADB."
    )
}

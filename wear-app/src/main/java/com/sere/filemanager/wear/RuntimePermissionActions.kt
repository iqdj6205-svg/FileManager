package com.sere.filemanager.wear

import androidx.activity.result.ActivityResultLauncher

class RuntimePermissionActions(
    private val launcher: ActivityResultLauncher<Array<String>>,
) {
    fun requestMedia() {
        launcher.launch(PermissionHelper.mediaPermissions())
    }

    fun requestNotifications() {
        val permissions = PermissionHelper.notificationPermissions()
        if (permissions.isNotEmpty()) launcher.launch(permissions)
    }
}

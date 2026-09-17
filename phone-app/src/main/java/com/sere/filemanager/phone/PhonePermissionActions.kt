package com.sere.filemanager.phone

import androidx.activity.result.ActivityResultLauncher

class PhonePermissionActions(
    private val launcher: ActivityResultLauncher<Array<String>>,
) {
    fun requestMedia() {
        launcher.launch(PhonePermissionHelper.mediaPermissions())
    }
}
package com.sere.filemanager.phone

import androidx.compose.runtime.Immutable

@Immutable
data class PhoneFileEditState(
    val renameValue: String = "",
    val createFolderValue: String = "New Folder",
    val message: String? = null,
)

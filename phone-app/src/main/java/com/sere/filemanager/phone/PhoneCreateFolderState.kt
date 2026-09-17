package com.sere.filemanager.phone

import androidx.compose.runtime.Immutable

@Immutable
data class PhoneCreateFolderState(
    val name: String = "New Folder",
    val message: String? = null,
)

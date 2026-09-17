package com.sere.filemanager.phone

import androidx.compose.runtime.Immutable
import com.sere.filemanager.core.model.FileItem

@Immutable
data class PhoneFileBrowserState(
    val currentPath: String = "/sdcard",
    val items: List<FileItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

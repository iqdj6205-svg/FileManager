package com.sere.filemanager.wear

import androidx.compose.runtime.Immutable
import com.sere.filemanager.core.model.FileItem

@Immutable
data class BrowserState(
    val currentPath: String = "/sdcard",
    val items: List<FileItem> = emptyList(),
    val selectedPath: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

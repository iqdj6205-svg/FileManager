package com.sere.filemanager.wear

import androidx.compose.runtime.Immutable
import com.sere.filemanager.core.files.FileClipboardMode

@Immutable
data class WearClipboardState(
    val fileName: String? = null,
    val mode: FileClipboardMode? = null,
    val message: String? = null,
) {
    val hasEntry: Boolean get() = fileName != null && mode != null
}

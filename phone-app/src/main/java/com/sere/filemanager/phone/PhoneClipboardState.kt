package com.sere.filemanager.phone

import androidx.compose.runtime.Immutable
import com.sere.filemanager.core.files.FileClipboardMode

@Immutable
data class PhoneClipboardState(
    val fileName: String? = null,
    val mode: FileClipboardMode? = null,
    val message: String? = null,
) {
    val hasEntry: Boolean get() = fileName != null && mode != null
}

package com.sere.filemanager.core.files

import com.sere.filemanager.core.model.FileItem

enum class FileClipboardMode { Copy, Move }

data class FileClipboardEntry(
    val item: FileItem,
    val mode: FileClipboardMode,
)

class FileClipboard {
    private var entry: FileClipboardEntry? = null
    fun set(item: FileItem, mode: FileClipboardMode) { entry = FileClipboardEntry(item, mode) }
    fun current(): FileClipboardEntry? = entry
    fun clear() { entry = null }
    val hasEntry: Boolean get() = entry != null
}

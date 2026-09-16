package com.sere.filemanager.core.files

import com.sere.filemanager.core.model.FileItem

data class FileDetails(
    val item: FileItem,
    val readableSize: String,
    val canRead: Boolean,
    val canWrite: Boolean,
    val absolutePath: String,
)

class FileDetailsReader {
    fun details(item: FileItem): FileDetails = FileDetails(
        item = item,
        readableSize = StorageFormatter.bytes(item.sizeBytes),
        canRead = true,
        canWrite = true,
        absolutePath = item.path,
    )
}

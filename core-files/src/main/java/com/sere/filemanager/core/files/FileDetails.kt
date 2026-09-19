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
    fun details(item: FileItem): FileDetails {
        val file = try { java.io.File(item.path) } catch (_: Exception) { null }
        val exists = file?.exists() == true
        return FileDetails(
            item = item,
            readableSize = StorageFormatter.bytes(item.sizeBytes),
            canRead = exists && file.canRead(),
            canWrite = exists && file.canWrite(),
            absolutePath = item.path,
        )
    }
}

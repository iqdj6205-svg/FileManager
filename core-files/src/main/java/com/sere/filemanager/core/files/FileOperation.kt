package com.sere.filemanager.core.files

sealed interface FileOperation {
    data class Copy(val sourcePath: String, val targetPath: String) : FileOperation
    data class Move(val sourcePath: String, val targetPath: String) : FileOperation
    data class Rename(val path: String, val newName: String) : FileOperation
    data class Delete(val path: String) : FileOperation
    data class CreateFolder(val parentPath: String, val name: String) : FileOperation
}

data class FileOperationResult(
    val operation: FileOperation,
    val success: Boolean,
    val message: String? = null,
)

package com.sere.filemanager.core.files

class FileOperationValidator {
    fun validate(operation: FileOperation): String? = when (operation) {
        is FileOperation.Copy -> validatePath(operation.sourcePath) ?: validatePath(operation.targetPath)
        is FileOperation.Move -> validatePath(operation.sourcePath) ?: validatePath(operation.targetPath)
        is FileOperation.Rename -> validatePath(operation.path).takeUnless { operation.newName.isBlank() }
            ?: if (operation.newName.isBlank()) "Name cannot be empty" else null
        is FileOperation.Delete -> validatePath(operation.path)
        is FileOperation.CreateFolder -> validatePath(operation.parentPath).takeUnless { operation.name.isBlank() }
            ?: if (operation.name.isBlank()) "Folder name cannot be empty" else null
    }

    private fun validatePath(path: String): String? = PathSafety.explainIfBlocked(path)
}

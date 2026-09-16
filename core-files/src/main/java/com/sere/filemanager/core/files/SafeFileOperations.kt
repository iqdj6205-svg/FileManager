package com.sere.filemanager.core.files

class SafeFileOperations(private val repository: FileRepository) {
    suspend fun execute(operation: FileOperation): FileOperationResult = when (operation) {
        is FileOperation.Copy -> repository.copy(operation.sourcePath, operation.targetPath)
            .toOperationResult(operation, "Copied")
        is FileOperation.Move -> repository.move(operation.sourcePath, operation.targetPath)
            .toOperationResult(operation, "Moved")
        is FileOperation.Rename -> repository.rename(operation.path, operation.newName)
            .toOperationResult(operation, "Renamed")
        is FileOperation.Delete -> repository.delete(operation.path)
            .toOperationResult(operation, "Deleted")
        is FileOperation.CreateFolder -> repository.createFolder(operation.parentPath, operation.name)
            .toOperationResult(operation, "Folder created")
    }

    private fun Result<Unit>.toOperationResult(operation: FileOperation, successMessage: String): FileOperationResult = fold(
        onSuccess = { FileOperationResult(operation, success = true, message = successMessage) },
        onFailure = { FileOperationResult(operation, success = false, message = it.message ?: "Operation failed") },
    )
}

package com.sere.filemanager.phone

import com.sere.filemanager.core.files.BrowserControllerCompat
import com.sere.filemanager.core.files.FileOperation
import com.sere.filemanager.core.files.SafeFileOperations
import com.sere.filemanager.core.model.FileItem

class PhoneFileOperationsController(
    private val operations: SafeFileOperations,
) {
    suspend fun copyHere(item: FileItem) = operations.execute(BrowserControllerCompat.copyOperation(item.path))
    suspend fun delete(item: FileItem) = operations.execute(FileOperation.Delete(item.path))
    suspend fun renameCopy(item: FileItem) = operations.execute(FileOperation.Rename(item.path, "copy-${item.name}"))
}

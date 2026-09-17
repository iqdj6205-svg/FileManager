package com.sere.filemanager.phone

import com.sere.filemanager.core.files.BrowserControllerCompat
import com.sere.filemanager.core.files.FileOperation
import com.sere.filemanager.core.files.FileOperationResult
import com.sere.filemanager.core.files.SafeFileOperations
import com.sere.filemanager.core.model.FileItem

class PhoneFileOperationsController(
    private val operations: SafeFileOperations,
    private val safController: PhoneSafController? = null,
) {
    suspend fun copyHere(item: FileItem): FileOperationResult = when {
        item.path.startsWith("content://") -> FileOperationResult(false, "Copy for SAF files will use stream copy")
        else -> operations.execute(BrowserControllerCompat.copyOperation(item.path))
    }

    suspend fun delete(item: FileItem): FileOperationResult = when {
        item.path.startsWith("content://") -> {
            val ok = safController?.delete(android.net.Uri.parse(item.path)) == true
            FileOperationResult(ok, if (ok) "Deleted ${item.name}" else "Cannot delete ${item.name}")
        }
        else -> operations.execute(FileOperation.Delete(item.path))
    }

    suspend fun renameCopy(item: FileItem): FileOperationResult = rename(item, "copy-${item.name}")

    suspend fun rename(item: FileItem, newName: String): FileOperationResult = when {
        item.path.startsWith("content://") -> {
            val ok = safController?.rename(android.net.Uri.parse(item.path), newName) == true
            FileOperationResult(ok, if (ok) "Renamed to $newName" else "Cannot rename ${item.name}")
        }
        else -> operations.execute(FileOperation.Rename(item.path, newName))
    }
}

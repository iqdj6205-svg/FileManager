package com.sere.filemanager.phone

import com.sere.filemanager.core.files.BrowserControllerCompat
import com.sere.filemanager.core.files.FileOperation
import com.sere.filemanager.core.files.FileOperationResult
import com.sere.filemanager.core.files.PathTools
import com.sere.filemanager.core.files.SafeFileOperations
import com.sere.filemanager.core.model.FileItem

class PhoneFileOperationsController(
    private val operations: SafeFileOperations,
    private val safController: PhoneSafController? = null,
) {
    suspend fun copyHere(item: FileItem): FileOperationResult = copyTo(item, PathTools.parent(item.path))

    suspend fun copyTo(item: FileItem, targetDirectory: String): FileOperationResult = when {
        item.path.startsWith("content://") || targetDirectory.startsWith("content://") -> FileOperationResult(false, "SAF stream copy is queued for next implementation block")
        else -> operations.execute(FileOperation.Copy(item.path, PathTools.child(targetDirectory, item.name)))
    }

    suspend fun moveTo(item: FileItem, targetDirectory: String): FileOperationResult = when {
        item.path.startsWith("content://") || targetDirectory.startsWith("content://") -> FileOperationResult(false, "SAF stream move is queued for next implementation block")
        else -> operations.execute(FileOperation.Move(item.path, PathTools.child(targetDirectory, item.name)))
    }

    suspend fun createFolder(parentPath: String, name: String): FileOperationResult = when {
        parentPath.startsWith("content://") -> {
            val ok = safController?.createFolder(android.net.Uri.parse(parentPath), name) == true
            FileOperationResult(ok, if (ok) "Created $name" else "Cannot create folder")
        }
        else -> operations.execute(BrowserControllerCompat.createFolderOperation(parentPath, name))
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

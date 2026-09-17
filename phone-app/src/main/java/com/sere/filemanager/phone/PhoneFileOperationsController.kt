package com.sere.filemanager.phone

import android.net.Uri
import com.sere.filemanager.core.files.BrowserControllerCompat
import com.sere.filemanager.core.files.FileOperation
import com.sere.filemanager.core.files.FileOperationResult
import com.sere.filemanager.core.files.OperationNamePolicy
import com.sere.filemanager.core.files.PathTools
import com.sere.filemanager.core.files.SafeFileOperations
import com.sere.filemanager.core.model.FileItem

class PhoneFileOperationsController(
    private val operations: SafeFileOperations,
    private val safController: PhoneSafController? = null,
) {
    suspend fun copyHere(item: FileItem): FileOperationResult = copyTo(item, PathTools.parent(item.path), OperationNamePolicy.duplicateName(item.name))

    suspend fun copyTo(item: FileItem, targetDirectory: String, targetName: String = item.name): FileOperationResult = when {
        item.path.startsWith("content://") && targetDirectory.startsWith("content://") -> safResult(safController?.copySafToSaf(Uri.parse(item.path), Uri.parse(targetDirectory), targetName) == true, "Copied $targetName")
        item.path.startsWith("content://") -> safResult(safController?.copySafToPath(Uri.parse(item.path), PathTools.child(targetDirectory, targetName)) == true, "Copied $targetName")
        targetDirectory.startsWith("content://") -> safResult(safController?.copyPathToSaf(item.path, Uri.parse(targetDirectory), targetName) == true, "Copied $targetName")
        else -> operations.execute(FileOperation.Copy(item.path, PathTools.child(targetDirectory, targetName)))
    }

    suspend fun moveTo(item: FileItem, targetDirectory: String): FileOperationResult = when {
        item.path.startsWith("content://") && targetDirectory.startsWith("content://") -> safResult(safController?.moveSafToSaf(Uri.parse(item.path), Uri.parse(targetDirectory), item.name) == true, "Moved ${item.name}")
        item.path.startsWith("content://") -> safResult(safController?.moveSafToPath(Uri.parse(item.path), PathTools.child(targetDirectory, item.name)) == true, "Moved ${item.name}")
        targetDirectory.startsWith("content://") -> safResult(safController?.movePathToSaf(item.path, Uri.parse(targetDirectory), item.name) == true, "Moved ${item.name}")
        else -> operations.execute(FileOperation.Move(item.path, PathTools.child(targetDirectory, item.name)))
    }

    suspend fun createFolder(parentPath: String, name: String): FileOperationResult {
        val safe = OperationNamePolicy.sanitizeInputName(name)
        if (!OperationNamePolicy.isValidFileName(safe)) return FileOperationResult(false, "Invalid folder name")
        return when {
            parentPath.startsWith("content://") -> safResult(safController?.createFolder(Uri.parse(parentPath), safe) == true, "Created $safe")
            else -> operations.execute(BrowserControllerCompat.createFolderOperation(parentPath, safe))
        }
    }

    suspend fun delete(item: FileItem): FileOperationResult = when {
        item.path.startsWith("content://") -> safResult(safController?.delete(Uri.parse(item.path)) == true, "Deleted ${item.name}")
        else -> operations.execute(FileOperation.Delete(item.path))
    }

    suspend fun renameCopy(item: FileItem): FileOperationResult = rename(item, OperationNamePolicy.duplicateName(item.name))

    suspend fun rename(item: FileItem, newName: String): FileOperationResult {
        val safe = OperationNamePolicy.sanitizeInputName(newName)
        if (!OperationNamePolicy.isValidFileName(safe)) return FileOperationResult(false, "Invalid file name")
        return when {
            item.path.startsWith("content://") -> safResult(safController?.rename(Uri.parse(item.path), safe) == true, "Renamed to $safe")
            else -> operations.execute(FileOperation.Rename(item.path, safe))
        }
    }

    private fun safResult(ok: Boolean, successMessage: String): FileOperationResult = FileOperationResult(ok, if (ok) successMessage else "SAF operation failed")
}

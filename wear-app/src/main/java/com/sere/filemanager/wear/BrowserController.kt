package com.sere.filemanager.wear

import com.sere.filemanager.core.files.FileOperation
import com.sere.filemanager.core.files.PathTools

object BrowserController {
    fun renameOperation(path: String, newName: String): FileOperation = FileOperation.Rename(path, newName)
    fun deleteOperation(path: String): FileOperation = FileOperation.Delete(path)
    fun copyOperation(path: String): FileOperation = FileOperation.Copy(path, PathTools.child(PathTools.parent(path), "copy-${PathTools.fileName(path)}"))
    fun moveOperation(path: String, targetDirectory: String): FileOperation = FileOperation.Move(path, PathTools.child(targetDirectory, PathTools.fileName(path)))
    fun createFolderOperation(parentPath: String, name: String): FileOperation = FileOperation.CreateFolder(parentPath, name)
}

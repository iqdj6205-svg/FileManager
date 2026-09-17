package com.sere.filemanager.core.files

object BrowserControllerCompat {
    fun copyOperation(path: String): FileOperation = FileOperation.Copy(path, PathTools.child(PathTools.parent(path), "copy-${PathTools.fileName(path)}"))
    fun moveOperation(path: String, targetDirectory: String): FileOperation = FileOperation.Move(path, PathTools.child(targetDirectory, PathTools.fileName(path)))
    fun createFolderOperation(parentPath: String, name: String): FileOperation = FileOperation.CreateFolder(parentPath, name)
}

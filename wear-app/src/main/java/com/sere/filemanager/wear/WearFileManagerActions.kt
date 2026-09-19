package com.sere.filemanager.wear

import com.sere.filemanager.core.files.FileClipboard
import com.sere.filemanager.core.files.FileClipboardMode
import com.sere.filemanager.core.files.FileOperation
import com.sere.filemanager.core.files.FileOperationResult
import com.sere.filemanager.core.files.OperationNamePolicy
import com.sere.filemanager.core.files.PathTools
import com.sere.filemanager.core.files.SafeFileOperations
import com.sere.filemanager.core.model.FileItem

class WearFileManagerActions(
    private val operations: SafeFileOperations,
    private val clipboard: FileClipboard = FileClipboard(),
) {
    fun mark(item: FileItem, mode: FileClipboardMode): WearClipboardState {
        clipboard.set(item, mode)
        return WearClipboardState(item.name, mode, "Queued ${item.name}", sourcePath = item.path)
    }

    fun clear(): WearClipboardState {
        clipboard.clear()
        return WearClipboardState(message = "Clipboard cleared")
    }

    suspend fun pasteInto(targetDirectory: String): Pair<FileOperationResult, WearClipboardState> {
        val entry = clipboard.current() ?: return FileOperationResult(false, "Clipboard empty") to WearClipboardState()
        val result = when (entry.mode) {
            FileClipboardMode.Copy -> operations.execute(FileOperation.Copy(entry.item.path, PathTools.child(targetDirectory, entry.item.name)))
            FileClipboardMode.Move -> operations.execute(FileOperation.Move(entry.item.path, PathTools.child(targetDirectory, entry.item.name)))
        }
        if (result.success) clipboard.clear()
        val state = if (result.success) WearClipboardState(message = "Clipboard cleared") else WearClipboardState(entry.item.name, entry.mode, result.message, sourcePath = entry.item.path)
        return result to state
    }

    suspend fun duplicate(item: FileItem): FileOperationResult {
        val name = OperationNamePolicy.duplicateName(item.name)
        return operations.execute(FileOperation.Copy(item.path, PathTools.child(PathTools.parent(item.path), name)))
    }
}

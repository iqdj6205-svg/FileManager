package com.sere.filemanager.core.files

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.sere.filemanager.core.model.FileItem
import com.sere.filemanager.core.model.FileItemType

class SafFileOperations(private val context: Context) {
    fun listTree(treeUri: Uri): List<FileItem> {
        val root = DocumentFile.fromTreeUri(context, treeUri) ?: return emptyList()
        return root.listFiles().map { it.toFileItem() }
            .sortedWith(compareBy<FileItem> { it.type != FileItemType.Directory }.thenBy { it.name.lowercase() })
    }

    fun createFolder(parentTreeUri: Uri, name: String): Boolean {
        val parent = DocumentFile.fromTreeUri(context, parentTreeUri) ?: return false
        return parent.createDirectory(name) != null
    }

    fun rename(documentUri: Uri, newName: String): Boolean {
        val doc = DocumentFile.fromSingleUri(context, documentUri) ?: DocumentFile.fromTreeUri(context, documentUri) ?: return false
        return doc.renameTo(newName)
    }

    fun delete(documentUri: Uri): Boolean {
        val doc = DocumentFile.fromSingleUri(context, documentUri) ?: DocumentFile.fromTreeUri(context, documentUri) ?: return false
        return doc.delete()
    }

    private fun DocumentFile.toFileItem(): FileItem = FileItem(
        name = name ?: "Unnamed",
        path = uri.toString(),
        type = if (isDirectory) FileItemType.Directory else classify(type),
        sizeBytes = if (isFile) length() else null,
        modifiedAtMillis = lastModified().takeIf { it > 0L },
    )

    private fun classify(mime: String?): FileItemType = when {
        mime == null -> FileItemType.Other
        mime.startsWith("image/") -> FileItemType.Image
        mime.startsWith("video/") -> FileItemType.Video
        mime.startsWith("audio/") -> FileItemType.Audio
        mime.contains("zip") || mime.contains("rar") || mime.contains("tar") -> FileItemType.Archive
        mime.contains("pdf") || mime.startsWith("text/") -> FileItemType.Document
        else -> FileItemType.Other
    }
}

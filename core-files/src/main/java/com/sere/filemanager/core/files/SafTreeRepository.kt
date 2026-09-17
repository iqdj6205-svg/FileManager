package com.sere.filemanager.core.files

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.sere.filemanager.core.model.FileItem
import com.sere.filemanager.core.model.FileItemType
import java.time.Instant

class SafTreeRepository(private val context: Context) {
    fun list(uri: Uri): List<FileItem> {
        val root = DocumentFile.fromTreeUri(context, uri) ?: return emptyList()
        return root.listFiles().map { doc ->
            FileItem(
                name = doc.name ?: "Unnamed",
                path = doc.uri.toString(),
                type = if (doc.isDirectory) FileItemType.Directory else classify(doc.type),
                sizeBytes = if (doc.isFile) doc.length() else null,
                modifiedAt = doc.lastModified().takeIf { it > 0L }?.let(Instant::ofEpochMilli),
            )
        }.sortedWith(compareBy<FileItem> { it.type != FileItemType.Directory }.thenBy { it.name.lowercase() })
    }

    private fun classify(mime: String?): FileItemType = when {
        mime == null -> FileItemType.Other
        mime.startsWith("image/") -> FileItemType.Image
        mime.startsWith("video/") -> FileItemType.Video
        mime.startsWith("audio/") -> FileItemType.Audio
        mime.contains("pdf") || mime.startsWith("text/") -> FileItemType.Document
        else -> FileItemType.Other
    }
}

package com.sere.filemanager.phone

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.sere.filemanager.core.files.SafFileOperations
import com.sere.filemanager.core.model.FileItem

class PhoneSafController(private val context: Context) {
    private val operations = SafFileOperations(context)

    fun persistAndName(uri: Uri): String {
        runCatching { context.contentResolver.takePersistableUriPermission(uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION) }
        return queryName(uri) ?: uri.lastPathSegment ?: "Storage folder"
    }

    fun list(uri: Uri): List<FileItem> = operations.listTree(uri)
    fun createFolder(parent: Uri, name: String): Boolean = operations.createFolder(parent, name)
    fun rename(uri: Uri, newName: String): Boolean = operations.rename(uri, newName)
    fun delete(uri: Uri): Boolean = operations.delete(uri)
    fun copySafToSaf(source: Uri, targetTree: Uri, name: String, mime: String = "application/octet-stream"): Boolean = operations.copyToTree(source, targetTree, name, mime)
    fun copySafToPath(source: Uri, targetPath: String): Boolean = operations.copyTreeDocumentToPath(source, targetPath)
    fun copyPathToSaf(sourcePath: String, targetTree: Uri, name: String? = null): Boolean = operations.copyPathToTree(sourcePath, targetTree, name)
    fun moveSafToSaf(source: Uri, targetTree: Uri, name: String, mime: String = "application/octet-stream"): Boolean = operations.moveToTree(source, targetTree, name, mime)
    fun moveSafToPath(source: Uri, targetPath: String): Boolean = operations.moveTreeDocumentToPath(source, targetPath)
    fun movePathToSaf(sourcePath: String, targetTree: Uri, name: String? = null): Boolean = operations.movePathToTree(sourcePath, targetTree, name)

    private fun queryName(uri: Uri): String? = context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (index >= 0 && cursor.moveToFirst()) cursor.getString(index) else null
    }
}

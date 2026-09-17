package com.sere.filemanager.core.files

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.sere.filemanager.core.model.FileItem
import com.sere.filemanager.core.model.FileItemType
import java.time.Instant

class SafFileOperations(
    private val context: Context,
    private val streamCopy: StreamCopy = StreamCopy(),
    private val progressSink: FileOperationProgressSink = InMemoryFileOperationProgressSink(),
    private val conflictResolver: SafConflictResolver = SafConflictResolver(),
) {
    val progress = progressSink.progress

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

    fun copyToTree(sourceUri: Uri, targetTreeUri: Uri, targetName: String, mimeType: String = "application/octet-stream"): Boolean {
        val parent = DocumentFile.fromTreeUri(context, targetTreeUri) ?: return false
        val decision = conflictResolver.resolve(parent, targetName)
        if (!decision.shouldWrite) return false
        if (decision.shouldReplace) decision.existing?.delete()
        val source = DocumentFile.fromSingleUri(context, sourceUri) ?: DocumentFile.fromTreeUri(context, sourceUri)
        val total = source?.length()?.takeIf { it > 0L }
        val target = parent.createFile(mimeType, decision.targetName) ?: return false
        return copyStreams(sourceUri, target.uri, "Copy", total)
    }

    fun copyTreeDocumentToPath(sourceUri: Uri, targetFilePath: String): Boolean {
        val target = java.io.File(FileConflictResolver().resolve(targetFilePath).targetPath)
        val source = DocumentFile.fromSingleUri(context, sourceUri) ?: DocumentFile.fromTreeUri(context, sourceUri)
        val total = source?.length()?.takeIf { it > 0L }
        target.parentFile?.mkdirs()
        return context.contentResolver.openInputStream(sourceUri)?.use { input ->
            target.outputStream().use { output -> streamCopy.copy(input, output, total) { done, all -> progressSink.update(FileOperationProgress("Copy", sourceUri.toString(), target.absolutePath, done, all)) } }
            progressSink.update(FileOperationProgress("Copy", sourceUri.toString(), target.absolutePath, total ?: target.length(), total, completed = true, message = "Copied ${target.name}"))
            true
        } ?: false
    }

    fun copyPathToTree(sourcePath: String, targetTreeUri: Uri, targetName: String? = null): Boolean {
        val source = java.io.File(sourcePath)
        if (!source.exists() || !source.isFile) return false
        val parent = DocumentFile.fromTreeUri(context, targetTreeUri) ?: return false
        val decision = conflictResolver.resolve(parent, targetName ?: source.name)
        if (!decision.shouldWrite) return false
        if (decision.shouldReplace) decision.existing?.delete()
        val target = parent.createFile(MimeTypeMap.guess(source.name), decision.targetName) ?: return false
        return context.contentResolver.openOutputStream(target.uri)?.use { output ->
            source.inputStream().use { input -> streamCopy.copy(input, output, source.length()) { done, all -> progressSink.update(FileOperationProgress("Copy", sourcePath, target.uri.toString(), done, all)) } }
            progressSink.update(FileOperationProgress("Copy", sourcePath, target.uri.toString(), source.length(), source.length(), completed = true, message = "Copied ${source.name}"))
            true
        } ?: false
    }

    fun moveToTree(sourceUri: Uri, targetTreeUri: Uri, targetName: String, mimeType: String = "application/octet-stream"): Boolean { val copied = copyToTree(sourceUri, targetTreeUri, targetName, mimeType); return copied && delete(sourceUri) }
    fun moveTreeDocumentToPath(sourceUri: Uri, targetFilePath: String): Boolean { val copied = copyTreeDocumentToPath(sourceUri, targetFilePath); return copied && delete(sourceUri) }
    fun movePathToTree(sourcePath: String, targetTreeUri: Uri, targetName: String? = null): Boolean { val copied = copyPathToTree(sourcePath, targetTreeUri, targetName); return copied && java.io.File(sourcePath).delete() }

    private fun copyStreams(source: Uri, target: Uri, label: String, total: Long?): Boolean = context.contentResolver.openInputStream(source)?.use { input ->
        context.contentResolver.openOutputStream(target)?.use { output -> streamCopy.copy(input, output, total) { done, all -> progressSink.update(FileOperationProgress(label, source.toString(), target.toString(), done, all)) } }
        progressSink.update(FileOperationProgress(label, source.toString(), target.toString(), total ?: 0L, total, completed = true, message = "$label complete"))
        true
    } ?: false

    private fun DocumentFile.toFileItem(): FileItem = FileItem(name = name ?: "Unnamed", path = uri.toString(), type = if (isDirectory) FileItemType.Directory else classify(type), sizeBytes = if (isFile) length() else null, modifiedAt = lastModified().takeIf { it > 0L }?.let(Instant::ofEpochMilli))
    private fun classify(mime: String?): FileItemType = when { mime == null -> FileItemType.Other; mime.startsWith("image/") -> FileItemType.Image; mime.startsWith("video/") -> FileItemType.Video; mime.startsWith("audio/") -> FileItemType.Audio; mime.contains("zip") || mime.contains("rar") || mime.contains("tar") -> FileItemType.Archive; mime.contains("pdf") || mime.startsWith("text/") -> FileItemType.Document; else -> FileItemType.Other }
}

object MimeTypeMap { fun guess(name: String): String = when (name.substringAfterLast('.', "").lowercase()) { "jpg", "jpeg" -> "image/jpeg"; "png" -> "image/png"; "webp" -> "image/webp"; "gif" -> "image/gif"; "mp3" -> "audio/mpeg"; "m4a" -> "audio/mp4"; "mp4" -> "video/mp4"; "pdf" -> "application/pdf"; "txt", "log", "md" -> "text/plain"; "zip" -> "application/zip"; else -> "application/octet-stream" } }

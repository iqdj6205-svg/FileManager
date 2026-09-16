package com.sere.filemanager.core.files

import com.sere.filemanager.core.model.FileItem
import com.sere.filemanager.core.model.FileItemType
import com.sere.filemanager.core.model.StorageVolume
import java.io.File
import java.time.Instant

class LocalFileRepository(
    private val roots: List<File> = listOf(File("/sdcard"), File("/storage/emulated/0")),
) : FileRepository {
    override suspend fun listRoots(): List<StorageVolume> = roots
        .filter { it.exists() }
        .map { file ->
            StorageVolume(
                id = file.absolutePath,
                label = file.name.ifBlank { file.absolutePath },
                path = file.absolutePath,
                totalBytes = file.totalSpace.takeIf { it > 0 },
                freeBytes = file.freeSpace.takeIf { it > 0 },
            )
        }

    override suspend fun list(path: String): List<FileItem> = File(path)
        .listFiles()
        ?.sortedWith(compareByDescending<File> { it.isDirectory }.thenBy { it.name.lowercase() })
        ?.map { it.toFileItem() }
        .orEmpty()

    override suspend fun createFolder(parentPath: String, name: String): Result<Unit> = runCatching {
        check(File(parentPath, name).mkdirs()) { "Cannot create folder" }
    }

    override suspend fun rename(path: String, newName: String): Result<Unit> = runCatching {
        val source = File(path)
        check(source.renameTo(File(source.parentFile, newName))) { "Cannot rename file" }
    }

    override suspend fun delete(path: String): Result<Unit> = runCatching {
        val file = File(path)
        if (file.isDirectory) file.deleteRecursively() else check(file.delete()) { "Cannot delete file" }
    }

    override suspend fun copy(sourcePath: String, targetPath: String): Result<Unit> = runCatching {
        val source = File(sourcePath)
        val target = File(targetPath)
        if (source.isDirectory) source.copyRecursively(target, overwrite = false) else source.copyTo(target, overwrite = false)
    }

    override suspend fun move(sourcePath: String, targetPath: String): Result<Unit> = runCatching {
        copy(sourcePath, targetPath).getOrThrow()
        delete(sourcePath).getOrThrow()
    }

    private fun File.toFileItem(): FileItem = FileItem(
        name = name,
        path = absolutePath,
        type = detectType(),
        sizeBytes = length().takeIf { isFile },
        modifiedAt = Instant.ofEpochMilli(lastModified()),
        isHidden = isHidden,
    )

    private fun File.detectType(): FileItemType {
        if (isDirectory) return FileItemType.Directory
        val ext = extension.lowercase()
        return when (ext) {
            "jpg", "jpeg", "png", "webp", "gif", "bmp", "heic" -> FileItemType.Image
            "mp4", "mkv", "webm", "avi", "mov" -> FileItemType.Video
            "mp3", "m4a", "wav", "ogg", "flac" -> FileItemType.Audio
            "zip", "rar", "7z", "tar", "gz" -> FileItemType.Archive
            "pdf", "txt", "md", "doc", "docx", "xls", "xlsx" -> FileItemType.Document
            else -> FileItemType.Other
        }
    }
}

package com.sere.filemanager.core.files

import com.sere.filemanager.core.model.FileItem
import com.sere.filemanager.core.model.FileItemType
import com.sere.filemanager.core.model.StorageVolume
import java.io.File
import java.time.Instant

class LocalFileRepository(
    private val roots: List<File> = listOf(File("/sdcard"), File("/storage/emulated/0")),
    private val conflictResolver: FileConflictResolver = FileConflictResolver(),
) : FileRepository {
    override suspend fun listRoots(): List<StorageVolume> = roots.filter { it.exists() }.map { file -> StorageVolume(id = file.absolutePath, label = file.name.ifBlank { file.absolutePath }, path = file.absolutePath, totalBytes = file.totalSpace.takeIf { it > 0 }, freeBytes = file.freeSpace.takeIf { it > 0 }) }
    override suspend fun list(path: String): List<FileItem> = File(path).listFiles()?.sortedWith(compareByDescending<File> { it.isDirectory }.thenBy { it.name.lowercase() })?.map { childToItem(it) }.orEmpty()
    override suspend fun createFolder(parentPath: String, name: String): Result<Unit> = runCatching { check(File(parentPath, name).mkdirs()) { "Cannot create folder" } }
    override suspend fun rename(path: String, newName: String): Result<Unit> = runCatching { val source = File(path); check(source.renameTo(File(source.parentFile, newName))) { "Cannot rename file" } }
    override suspend fun delete(path: String): Result<Unit> = runCatching { val file = File(path); if (file.isDirectory) file.deleteRecursively() else check(file.delete()) { "Cannot delete file" } }
    override suspend fun copy(sourcePath: String, targetPath: String): Result<Unit> = runCatching {
        val source = File(sourcePath)
        val decision = conflictResolver.resolve(targetPath)
        if (!decision.shouldWrite) error(decision.message ?: "Copy skipped")
        val target = File(decision.targetPath)
        if (decision.shouldReplace && target.exists()) { if (target.isDirectory) target.deleteRecursively() else target.delete() }
        if (source.isDirectory) source.copyRecursively(target, overwrite = decision.shouldReplace) else source.copyTo(target, overwrite = decision.shouldReplace)
    }
    override suspend fun move(sourcePath: String, targetPath: String): Result<Unit> = runCatching { copy(sourcePath, targetPath).getOrThrow(); delete(sourcePath).getOrThrow() }
    fun childToItem(file: File): FileItem = FileItem(name = file.name, path = file.absolutePath, type = file.detectType(), sizeBytes = file.length().takeIf { file.isFile }, modifiedAt = Instant.ofEpochMilli(file.lastModified()), isHidden = file.isHidden)
    private fun File.detectType(): FileItemType { if (isDirectory) return FileItemType.Directory; return com.sere.filemanager.core.model.FileTypeClassifier.fromExtension(extension) }
}

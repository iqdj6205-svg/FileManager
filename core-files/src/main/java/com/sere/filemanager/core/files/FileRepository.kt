package com.sere.filemanager.core.files

import com.sere.filemanager.core.model.FileItem
import com.sere.filemanager.core.model.StorageVolume

interface FileRepository {
    suspend fun listRoots(): List<StorageVolume>
    suspend fun list(path: String): List<FileItem>
    suspend fun createFolder(parentPath: String, name: String): Result<Unit>
    suspend fun rename(path: String, newName: String): Result<Unit>
    suspend fun delete(path: String): Result<Unit>
    suspend fun copy(sourcePath: String, targetPath: String): Result<Unit>
    suspend fun move(sourcePath: String, targetPath: String): Result<Unit>
}

package com.sere.filemanager.core.files

import com.sere.filemanager.core.model.FileItem
import com.sere.filemanager.core.model.FileItemType

data class StorageCategorySummary(
    val type: FileItemType,
    val count: Int,
    val totalBytes: Long,
)

data class StorageAnalysis(
    val path: String,
    val totalFiles: Int,
    val totalBytes: Long,
    val categories: List<StorageCategorySummary>,
    val largestFiles: List<FileItem>,
)

class StorageAnalyzer(
    private val repository: FileRepository,
    private val scanner: RecursiveScanner = RecursiveScanner(maxFiles = 1_000),
) {
    suspend fun analyze(path: String): StorageAnalysis {
        if (PathSafety.explainIfBlocked(path) != null) return StorageAnalysis(path, 0, 0, emptyList(), emptyList())
        val items = try { scanner.scan(path) } catch (_: Exception) { repository.list(path) }
        val files = items.filter { it.type != FileItemType.Directory }
        val categories = files
            .groupBy { it.type }
            .map { (type, group) -> StorageCategorySummary(type, group.size, group.sumOf { it.sizeBytes ?: 0L }) }
            .sortedByDescending { it.totalBytes }
        return StorageAnalysis(
            path = path,
            totalFiles = files.size,
            totalBytes = files.sumOf { it.sizeBytes ?: 0L },
            categories = categories,
            largestFiles = files.sortedByDescending { it.sizeBytes ?: 0L }.take(20),
        )
    }
}

package com.sere.filemanager.core.files

import com.sere.filemanager.core.model.FileItem

class FileSearch(
    private val repository: FileRepository,
    private val scanner: RecursiveScanner = RecursiveScanner(maxFiles = 1_000),
) {
    suspend fun search(path: String, query: String): List<FileItem> {
        if (query.isBlank()) return emptyList()
        if (PathSafety.explainIfBlocked(path) != null) return emptyList()
        val items = try { scanner.scan(path) } catch (_: Exception) { repository.list(path) }
        return items.filter { item ->
            item.name.contains(query, ignoreCase = true)
        }
    }
}

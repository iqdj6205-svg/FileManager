package com.sere.filemanager.core.files

import com.sere.filemanager.core.model.FileItem

class FileSearch(private val repository: FileRepository) {
    suspend fun search(path: String, query: String): List<FileItem> {
        if (query.isBlank()) return emptyList()
        return repository.list(path).filter { item ->
            item.name.contains(query, ignoreCase = true)
        }
    }
}

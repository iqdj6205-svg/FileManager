package com.sere.filemanager.core.files

import com.sere.filemanager.core.model.FileItem
import com.sere.filemanager.core.model.FileItemType

class FileBrowserUseCase(
    private val repository: FileRepository,
) {
    suspend fun loadDirectory(path: String, sortMode: SortMode = SortMode.NameAsc, showHidden: Boolean = false): List<FileItem> {
        return repository.list(path)
            .filter { showHidden || !it.isHidden }
            .sortedWith(sortMode.comparator)
    }
}

enum class SortMode(val label: String, val comparator: Comparator<FileItem>) {
    NameAsc("Name", compareByDescending<FileItem> { it.type == FileItemType.Directory }.thenBy { it.name.lowercase() }),
    NameDesc("Name desc", compareByDescending<FileItem> { it.type == FileItemType.Directory }.thenByDescending { it.name.lowercase() }),
    SizeDesc("Size", compareByDescending<FileItem> { it.type == FileItemType.Directory }.thenByDescending { it.sizeBytes ?: -1L }),
    DateDesc("Date", compareByDescending<FileItem> { it.type == FileItemType.Directory }.thenByDescending { it.modifiedAt }),
    TypeAsc("Type", compareByDescending<FileItem> { it.type == FileItemType.Directory }.thenBy { it.type.name }.thenBy { it.name.lowercase() }),
}

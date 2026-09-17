package com.sere.filemanager.core.files

import com.sere.filemanager.core.model.DuplicateCandidate
import com.sere.filemanager.core.model.FileItem
import com.sere.filemanager.core.model.FileItemType

class DuplicateFinder {
    fun findCandidates(items: List<FileItem>): List<DuplicateCandidate> {
        return items
            .filter { it.type != FileItemType.Directory && (it.sizeBytes ?: 0L) > 0L }
            .groupBy { it.name.lowercase() to (it.sizeBytes ?: 0L) }
            .values
            .filter { it.size > 1 }
            .map { group -> DuplicateCandidate(group.first().name, group.first().sizeBytes ?: 0L, group.map { it.path }) }
            .sortedByDescending { it.sizeBytes * it.paths.size }
    }
}

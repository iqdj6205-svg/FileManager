package com.sere.filemanager.core.files

import com.sere.filemanager.core.model.StorageInsight
import com.sere.filemanager.core.model.StorageInsightSeverity

class StorageInsights {
    fun fromAnalysis(analysis: StorageAnalysis): List<StorageInsight> = buildList {
        if (analysis.totalFiles == 0) add(StorageInsight("No files visible", "Grant permissions or choose another folder."))
        val largest = analysis.largestFiles.firstOrNull()
        if (largest != null && (largest.sizeBytes ?: 0L) > 100L * 1024L * 1024L) {
            add(StorageInsight("Large file found", "${largest.name} uses ${StorageFormatter.bytes(largest.sizeBytes)}", StorageInsightSeverity.Warning))
        }
        val archives = analysis.categories.firstOrNull { it.type.name == "Archive" }
        if (archives != null && archives.totalBytes > 0) add(StorageInsight("Archives use space", "${archives.count} archives use ${StorageFormatter.bytes(archives.totalBytes)}"))
    }
}

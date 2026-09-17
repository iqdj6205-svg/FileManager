package com.sere.filemanager.core.model

data class StorageInsight(
    val title: String,
    val description: String,
    val severity: StorageInsightSeverity = StorageInsightSeverity.Info,
)

enum class StorageInsightSeverity { Info, Warning, Critical }

data class DuplicateCandidate(
    val name: String,
    val sizeBytes: Long,
    val paths: List<String>,
)

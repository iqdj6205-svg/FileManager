package com.sere.filemanager.core.model

enum class FileSortMode { NameAsc, NameDesc, SizeDesc, DateDesc, TypeAsc }

data class BrowserPreferences(
    val showHiddenFiles: Boolean = false,
    val sortMode: FileSortMode = FileSortMode.NameAsc,
    val favorites: Set<String> = emptySet(),
    val recentPaths: List<String> = emptyList(),
)

package com.sere.filemanager.core.media

data class MediaLibraryState(
    val images: List<MediaItem> = emptyList(),
    val audio: List<MediaItem> = emptyList(),
    val video: List<MediaItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

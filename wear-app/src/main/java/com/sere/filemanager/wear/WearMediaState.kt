package com.sere.filemanager.wear

import androidx.compose.runtime.Immutable
import com.sere.filemanager.core.media.MediaItem

@Immutable
data class WearMediaState(
    val images: List<MediaItem> = emptyList(),
    val audio: List<MediaItem> = emptyList(),
    val video: List<MediaItem> = emptyList(),
    val selected: MediaItem? = null,
    val isLoading: Boolean = false,
    val message: String? = null,
)

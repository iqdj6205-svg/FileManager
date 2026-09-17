package com.sere.filemanager.phone

import androidx.compose.runtime.Immutable
import com.sere.filemanager.core.media.MediaBucket
import com.sere.filemanager.core.media.MediaItem

@Immutable
data class PhoneMediaState(
    val buckets: List<MediaBucket> = emptyList(),
    val items: List<MediaItem> = emptyList(),
    val selected: MediaItem? = null,
    val isLoading: Boolean = false,
    val message: String? = null,
)

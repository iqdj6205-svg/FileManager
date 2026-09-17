package com.sere.filemanager.core.media

data class MediaLibraryState(
    val images: List<MediaItem> = emptyList(),
    val audio: List<MediaItem> = emptyList(),
    val video: List<MediaItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
) {
    val items: List<MediaItem> get() = buildList { addAll(images); addAll(audio); addAll(video) }
    val buckets: List<MediaBucket>
        get() = items.groupBy { it.bucketName ?: "Other" }
            .map { (name, list) -> MediaBucket(name = name, itemCount = list.size, coverPath = list.firstOrNull()?.uri) }
}

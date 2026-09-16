package com.sere.filemanager.core.media

data class MediaStoreQuery(
    val includeImages: Boolean = true,
    val includeVideo: Boolean = true,
    val includeAudio: Boolean = true,
    val limit: Int = 200,
)

data class MediaBucket(
    val name: String,
    val itemCount: Int,
    val coverPath: String? = null,
)

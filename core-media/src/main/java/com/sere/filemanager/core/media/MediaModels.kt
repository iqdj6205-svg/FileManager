package com.sere.filemanager.core.media

data class MediaItem(
    val id: String,
    val path: String,
    val title: String,
    val mimeType: String? = null,
    val durationMillis: Long? = null,
)

package com.sere.filemanager.core.model

import java.time.Instant

enum class FileItemType { Directory, Image, Video, Audio, Document, Archive, Other }

data class FileItem(
    val name: String,
    val path: String,
    val type: FileItemType,
    val sizeBytes: Long? = null,
    val modifiedAt: Instant? = null,
    val isHidden: Boolean = false,
)

package com.sere.filemanager.core.media

import com.sere.filemanager.core.model.FileItem
import com.sere.filemanager.core.model.FileItemType

object MediaClassifier {
    fun isImage(item: FileItem): Boolean = item.type == FileItemType.Image
    fun isAudio(item: FileItem): Boolean = item.type == FileItemType.Audio
    fun isVideo(item: FileItem): Boolean = item.type == FileItemType.Video
    fun isPlayable(item: FileItem): Boolean = isAudio(item) || isVideo(item)
}

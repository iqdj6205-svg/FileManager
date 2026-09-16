package com.sere.filemanager.core.files

import com.sere.filemanager.core.model.FileItemType

data class FileOpenDecision(
    val canPreviewInApp: Boolean,
    val shouldUseExternalApp: Boolean,
    val reason: String? = null,
)

object FileOpenPolicy {
    fun decide(type: FileItemType): FileOpenDecision = when (type) {
        FileItemType.Image -> FileOpenDecision(canPreviewInApp = true, shouldUseExternalApp = false)
        FileItemType.Audio -> FileOpenDecision(canPreviewInApp = true, shouldUseExternalApp = false)
        FileItemType.Video -> FileOpenDecision(canPreviewInApp = true, shouldUseExternalApp = false, reason = "Video can drain watch battery quickly")
        FileItemType.Document, FileItemType.Archive, FileItemType.Other -> FileOpenDecision(canPreviewInApp = false, shouldUseExternalApp = true)
        FileItemType.Directory -> FileOpenDecision(canPreviewInApp = false, shouldUseExternalApp = false)
    }
}

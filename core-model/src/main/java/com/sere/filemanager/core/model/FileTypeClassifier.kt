package com.sere.filemanager.core.model

object FileTypeClassifier {
    fun fromExtension(ext: String): FileItemType {
        return when (ext.lowercase()) {
            "jpg", "jpeg", "png", "webp", "gif", "bmp", "heic" -> FileItemType.Image
            "mp4", "mkv", "webm", "avi", "mov" -> FileItemType.Video
            "mp3", "m4a", "wav", "ogg", "flac" -> FileItemType.Audio
            "zip", "rar", "7z", "tar", "gz" -> FileItemType.Archive
            "pdf", "txt", "md", "doc", "docx", "xls", "xlsx" -> FileItemType.Document
            else -> FileItemType.Other
        }
    }

    fun fromMime(mime: String?): FileItemType = when {
        mime == null -> FileItemType.Other
        mime.startsWith("image/") -> FileItemType.Image
        mime.startsWith("video/") -> FileItemType.Video
        mime.startsWith("audio/") -> FileItemType.Audio
        mime.contains("zip") || mime.contains("rar") || mime.contains("tar") -> FileItemType.Archive
        mime.contains("pdf") || mime.startsWith("text/") -> FileItemType.Document
        else -> FileItemType.Other
    }
}

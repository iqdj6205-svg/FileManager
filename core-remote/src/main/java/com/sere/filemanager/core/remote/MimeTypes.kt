package com.sere.filemanager.core.remote

object MimeTypes {
    fun fromFileName(name: String): String = when (name.substringAfterLast('.', "").lowercase()) {
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "gif" -> "image/gif"
        "webp" -> "image/webp"
        "mp3" -> "audio/mpeg"
        "m4a" -> "audio/mp4"
        "wav" -> "audio/wav"
        "mp4" -> "video/mp4"
        "webm" -> "video/webm"
        "pdf" -> "application/pdf"
        "txt", "log", "md" -> "text/plain; charset=utf-8"
        "html", "htm" -> "text/html; charset=utf-8"
        "json" -> "application/json; charset=utf-8"
        "zip" -> "application/zip"
        else -> "application/octet-stream"
    }
}

package com.sere.filemanager.core.remote

import java.io.ByteArrayInputStream

class RemoteUploadRequestParser {
    fun rawBody(request: HttpRequest): ByteArrayInputStream {
        val length = request.contentLength()?.coerceAtMost(25L * 1024L * 1024L)?.toInt() ?: 0
        if (length <= 0) return ByteArrayInputStream(ByteArray(0))
        val chars = CharArray(length)
        val read = request.bodyReader.read(chars, 0, length).coerceAtLeast(0)
        return ByteArrayInputStream(String(chars, 0, read).toByteArray())
    }
}

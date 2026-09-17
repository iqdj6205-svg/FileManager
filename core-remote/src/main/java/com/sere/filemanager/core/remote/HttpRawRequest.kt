package com.sere.filemanager.core.remote

import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.InputStream

data class HttpRawRequest(
    val method: String,
    val path: String,
    val headers: Map<String, String>,
    val body: InputStream,
) {
    fun contentLength(): Long? = headers["content-length"]?.toLongOrNull()
    fun contentType(): String? = headers["content-type"]
}

object HttpRawRequestParser {
    fun parse(input: InputStream): HttpRawRequest {
        val buffered = BufferedInputStream(input)
        val requestLine = readAsciiLine(buffered)
        val parts = requestLine.split(" ")
        val headers = mutableMapOf<String, String>()
        while (true) {
            val line = readAsciiLine(buffered)
            if (line.isEmpty()) break
            val key = line.substringBefore(':').trim().lowercase()
            val value = line.substringAfter(':', "").trim()
            if (key.isNotBlank()) headers[key] = value
        }
        return HttpRawRequest(
            method = parts.getOrNull(0).orEmpty(),
            path = parts.getOrNull(1).orEmpty(),
            headers = headers,
            body = buffered,
        )
    }

    private fun readAsciiLine(input: InputStream): String {
        val bytes = ArrayList<Byte>()
        while (true) {
            val b = input.read()
            if (b < 0) break
            if (b == '\n'.code) break
            if (b != '\r'.code) bytes.add(b.toByte())
        }
        return bytes.toByteArray().toString(Charsets.ISO_8859_1)
    }
}

class LimitedInputStream(
    private val delegate: InputStream,
    private val limitBytes: Long,
) : InputStream() {
    private var readBytes = 0L
    override fun read(): Int {
        if (readBytes >= limitBytes) return -1
        val value = delegate.read()
        if (value >= 0) readBytes++
        return value
    }

    override fun read(buffer: ByteArray, offset: Int, length: Int): Int {
        if (readBytes >= limitBytes) return -1
        val allowed = minOf(length.toLong(), limitBytes - readBytes).toInt()
        val count = delegate.read(buffer, offset, allowed)
        if (count > 0) readBytes += count
        return count
    }
}

object EmptyBody { fun stream(): InputStream = ByteArrayInputStream(ByteArray(0)) }

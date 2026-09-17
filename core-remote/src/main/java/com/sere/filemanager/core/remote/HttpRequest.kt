package com.sere.filemanager.core.remote

import java.io.BufferedReader

data class HttpRequest(
    val method: String,
    val path: String,
    val headers: Map<String, String>,
    val bodyReader: BufferedReader,
) {
    fun contentLength(): Long? = headers["content-length"]?.toLongOrNull()
}

object HttpRequestParser {
    fun parse(requestLine: String, input: BufferedReader): HttpRequest {
        val parts = requestLine.split(" ")
        val headers = mutableMapOf<String, String>()
        while (true) {
            val line = input.readLine().orEmpty()
            if (line.isEmpty()) break
            val key = line.substringBefore(':').trim().lowercase()
            val value = line.substringAfter(':', "").trim()
            if (key.isNotBlank()) headers[key] = value
        }
        return HttpRequest(
            method = parts.getOrNull(0).orEmpty(),
            path = parts.getOrNull(1).orEmpty(),
            headers = headers,
            bodyReader = input,
        )
    }
}

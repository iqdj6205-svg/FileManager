package com.sere.filemanager.core.remote

import com.sere.filemanager.core.files.FileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.net.ServerSocket
import java.net.Socket

/**
 * Minimal blocking HTTP engine suitable for a first prototype.
 * Production hardening should add streaming, MIME detection, upload parsing,
 * request limits, foreground-service integration, and better error handling.
 */
class SimpleHttpEngine(
    private val fileRepository: FileRepository,
    private val config: RemoteConfig = RemoteConfig(),
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
) {
    private var serverSocket: ServerSocket? = null
    private var job: Job? = null
    private val auth = RemoteAuth()
    private var pin: String? = null

    fun start(sessionPin: String) {
        if (job?.isActive == true) return
        pin = sessionPin
        job = scope.launch {
            serverSocket = ServerSocket(config.port)
            while (job?.isActive == true) {
                val socket = serverSocket?.accept() ?: break
                launch { handle(socket) }
            }
        }
    }

    fun stop() {
        runCatching { serverSocket?.close() }
        serverSocket = null
        job?.cancel()
        job = null
    }

    private suspend fun handle(socket: Socket) {
        socket.use { client ->
            val input = client.getInputStream().bufferedReader()
            val output = client.getOutputStream()
            val requestLine = input.readLine().orEmpty()
            val parts = requestLine.split(" ")
            val path = parts.getOrNull(1).orEmpty()
            val response = route(path)
            output.write(response.toByteArray())
            output.flush()
        }
    }

    private suspend fun route(path: String): String {
        return when {
            path == "/" -> okHtml(WebManagerPage.html())
            path.startsWith(RemoteRoutes.API_STATUS) -> okJson("{\"status\":\"running\"}")
            path.startsWith(RemoteRoutes.API_LIST) -> {
                val requestedPath = path.substringAfter("path=", "/sdcard")
                val items = fileRepository.list(requestedPath).joinToString(prefix = "[", postfix = "]") {
                    "{\"name\":\"${it.name.escapeJson()}\",\"path\":\"${it.path.escapeJson()}\",\"type\":\"${it.type}\"}"
                }
                okJson(items)
            }
            else -> notFound()
        }
    }

    private fun okHtml(body: String) = "HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=utf-8\r\nContent-Length: ${body.toByteArray().size}\r\n\r\n$body"
    private fun okJson(body: String) = "HTTP/1.1 200 OK\r\nContent-Type: application/json; charset=utf-8\r\nContent-Length: ${body.toByteArray().size}\r\n\r\n$body"
    private fun notFound() = "HTTP/1.1 404 Not Found\r\nContent-Length: 9\r\n\r\nNot Found"
    private fun String.escapeJson(): String = replace("\\", "\\\\").replace("\"", "\\\"")
}

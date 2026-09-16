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
 * Production hardening should add streaming, upload parsing, request limits,
 * foreground-service integration, and stronger error handling.
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
            while (input.readLine().orEmpty().isNotEmpty()) {
                // Drain headers for this simple prototype.
            }
            val parts = requestLine.split(" ")
            val path = parts.getOrNull(1).orEmpty()
            val response = runCatching { route(path) }.getOrElse { HttpResponses.serverError(it.message ?: "Server error") }
            output.write(response.toByteArray())
            output.flush()
        }
    }

    private suspend fun route(path: String): String {
        val route = HttpRequestTools.routePath(path)
        return when (route) {
            RemoteRoutes.INDEX -> HttpResponses.html(WebManagerPage.html())
            RemoteRoutes.API_STATUS -> HttpResponses.json("{\"status\":\"running\"}")
            RemoteRoutes.API_LIST -> listResponse(path)
            RemoteRoutes.API_DOWNLOAD -> downloadResponse(path)
            else -> HttpResponses.notFound()
        }
    }

    private suspend fun listResponse(path: String): String {
        val requestedPath = HttpRequestTools.queryParam(path, "path") ?: "/sdcard"
        val providedPin = HttpRequestTools.queryParam(path, "pin")
        if (config.requirePin && !auth.isPinValid(pin, providedPin)) return HttpResponses.unauthorized()
        val items = fileRepository.list(requestedPath).joinToString(prefix = "[", postfix = "]") {
            "{\"name\":\"${it.name.escapeJson()}\",\"path\":\"${it.path.escapeJson()}\",\"type\":\"${it.type}\",\"sizeBytes\":${it.sizeBytes ?: 0}}"
        }
        return HttpResponses.json(items)
    }

    private fun downloadResponse(path: String): String {
        val providedPin = HttpRequestTools.queryParam(path, "pin")
        if (config.requirePin && !auth.isPinValid(pin, providedPin)) return HttpResponses.unauthorized()
        return HttpResponses.text("Download streaming will be enabled in the hardened server implementation.")
    }

    private fun String.escapeJson(): String = replace("\\", "\\\\").replace("\"", "\\\"")
}

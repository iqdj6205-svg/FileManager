package com.sere.filemanager.core.remote

import com.sere.filemanager.core.files.FileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.net.ServerSocket
import java.net.Socket

/** Minimal blocking HTTP engine for the first prototype. */
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
            while (input.readLine().orEmpty().isNotEmpty()) Unit
            val parts = requestLine.split(" ")
            val method = parts.getOrNull(0).orEmpty()
            val path = parts.getOrNull(1).orEmpty()
            val response = runCatching { route(method, path) }.getOrElse { HttpResponses.serverError(it.message ?: "Server error") }
            output.write(response.toByteArray())
            output.flush()
        }
    }

    private suspend fun route(method: String, path: String): String {
        val route = HttpRequestTools.routePath(path)
        return when (route) {
            RemoteRoutes.INDEX -> if (method == "GET") HttpResponses.html(WebManagerPage.html()) else HttpResponses.badRequest("Unsupported method")
            RemoteRoutes.API_STATUS -> if (method == "GET") HttpResponses.json("{\"status\":\"running\"}") else HttpResponses.badRequest("Unsupported method")
            RemoteRoutes.API_LIST -> if (method == "GET") listResponse(path) else HttpResponses.badRequest("Unsupported method")
            RemoteRoutes.API_DOWNLOAD -> if (method == "GET") downloadResponse(path) else HttpResponses.badRequest("Unsupported method")
            else -> HttpResponses.notFound()
        }
    }

    private suspend fun listResponse(path: String): String {
        val requestedPath = HttpRequestTools.queryParam(path, "path") ?: "/sdcard"
        val validation = RemotePathGuard.validate(requestedPath)
        if (validation != null) return HttpResponses.forbidden(validation)
        val providedPin = HttpRequestTools.queryParam(path, "pin")
        if (config.requirePin && !auth.isPinValid(pin, providedPin)) return HttpResponses.unauthorized()
        return HttpResponses.json(RemoteDirectorySerializer.serialize(fileRepository.list(requestedPath)))
    }

    private fun downloadResponse(path: String): String {
        val requestedPath = HttpRequestTools.queryParam(path, "path") ?: return HttpResponses.badRequest("Missing path")
        val validation = RemotePathGuard.validate(requestedPath)
        if (validation != null) return HttpResponses.forbidden(validation)
        val providedPin = HttpRequestTools.queryParam(path, "pin")
        if (config.requirePin && !auth.isPinValid(pin, providedPin)) return HttpResponses.unauthorized()
        return HttpResponses.text("Download streaming reserved for: $requestedPath")
    }
}

package com.sere.filemanager.core.remote

import com.sere.filemanager.core.files.FileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.File
import java.net.ServerSocket
import java.net.Socket

/** Minimal blocking HTTP engine for the first prototype. */
class SimpleHttpEngine(
    private val fileRepository: FileRepository,
    private val config: RemoteConfig = RemoteConfig(),
    private val auditLog: RemoteAuditLog = InMemoryRemoteAuditLog(),
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
) {
    private var serverSocket: ServerSocket? = null
    private var job: Job? = null
    private val auth = RemoteAuth()
    private val downloadPlanner = DownloadPlanner()
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
            val response = runCatching { route(method, path) }.getOrElse {
                auditLog.record(RemoteAuditEvent(RemoteAuditEventType.Error, it.message ?: "Server error"))
                HttpResponseFactory.serverError(it.message ?: "Server error")
            }
            HttpResponseWriter.write(output, response)
            output.flush()
        }
    }

    private suspend fun route(method: String, path: String): HttpResponse {
        val route = HttpRequestTools.routePath(path)
        return when (route) {
            RemoteRoutes.INDEX -> if (method == "GET") HttpResponseFactory.html(WebManagerPage.html()) else HttpResponseFactory.badRequest("Unsupported method")
            RemoteRoutes.API_STATUS -> if (method == "GET") HttpResponseFactory.json("{\"status\":\"running\"}") else HttpResponseFactory.badRequest("Unsupported method")
            RemoteRoutes.API_LIST -> if (method == "GET") listResponse(path) else HttpResponseFactory.badRequest("Unsupported method")
            RemoteRoutes.API_DOWNLOAD -> if (method == "GET") downloadResponse(path) else HttpResponseFactory.badRequest("Unsupported method")
            else -> HttpResponseFactory.notFound()
        }
    }

    private suspend fun listResponse(path: String): HttpResponse {
        val requestedPath = HttpRequestTools.queryParam(path, "path") ?: "/sdcard"
        val validation = RemotePathGuard.validate(requestedPath)
        if (validation != null) return HttpResponseFactory.forbidden(validation)
        val providedPin = HttpRequestTools.queryParam(path, "pin")
        if (config.requirePin && !auth.isPinValid(pin, providedPin)) return HttpResponseFactory.unauthorized()
        auditLog.record(RemoteAuditEvent(RemoteAuditEventType.ListDirectory, "Directory listed", requestedPath))
        return HttpResponseFactory.json(RemoteDirectorySerializer.serialize(fileRepository.list(requestedPath)))
    }

    private fun downloadResponse(path: String): HttpResponse {
        val requestedPath = HttpRequestTools.queryParam(path, "path") ?: return HttpResponseFactory.badRequest("Missing path")
        val providedPin = HttpRequestTools.queryParam(path, "pin")
        if (config.requirePin && !auth.isPinValid(pin, providedPin)) return HttpResponseFactory.unauthorized()
        val plan = downloadPlanner.plan(requestedPath).getOrElse { return HttpResponseFactory.badRequest(it.message ?: "Cannot download") }
        auditLog.record(RemoteAuditEvent(RemoteAuditEventType.DownloadRequested, "Download requested", plan.path))
        return HttpResponse.FileStream(file = File(plan.path), contentType = plan.mimeType, downloadName = plan.fileName)
    }
}

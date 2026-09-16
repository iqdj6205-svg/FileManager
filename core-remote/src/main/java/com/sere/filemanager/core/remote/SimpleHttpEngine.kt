package com.sere.filemanager.core.remote

import com.sere.filemanager.core.files.FileRepository
import com.sere.filemanager.core.files.SafeFileOperations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.File
import java.net.ServerSocket
import java.net.Socket

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
    private val writeController = RemoteWriteController(SafeFileOperations(fileRepository), config, auditLog)
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

    fun stop() { runCatching { serverSocket?.close() }; serverSocket = null; job?.cancel(); job = null }

    private suspend fun handle(socket: Socket) {
        socket.use { client ->
            val input = client.getInputStream().bufferedReader()
            val output = client.getOutputStream()
            val requestLine = input.readLine().orEmpty()
            while (input.readLine().orEmpty().isNotEmpty()) Unit
            val parts = requestLine.split(" ")
            val method = parts.getOrNull(0).orEmpty()
            val path = parts.getOrNull(1).orEmpty()
            val response = runCatching { route(method, path) }.getOrElse { auditLog.record(RemoteAuditEvent(RemoteAuditEventType.Error, it.message ?: "Server error")); HttpResponseFactory.serverError(it.message ?: "Server error") }
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
            RemoteRoutes.API_RENAME -> if (method == "POST") renameResponse(path) else HttpResponseFactory.badRequest("Use POST")
            RemoteRoutes.API_DELETE -> if (method == "POST") deleteResponse(path) else HttpResponseFactory.badRequest("Use POST")
            RemoteRoutes.API_MKDIR -> if (method == "POST") mkdirResponse(path) else HttpResponseFactory.badRequest("Use POST")
            RemoteRoutes.API_UPLOAD -> if (method == "POST") uploadResponse(path) else HttpResponseFactory.badRequest("Use POST")
            else -> HttpResponseFactory.notFound()
        }
    }

    private fun authorized(path: String): Boolean = !config.requirePin || auth.isPinValid(pin, HttpRequestTools.queryParam(path, "pin"))

    private suspend fun listResponse(path: String): HttpResponse {
        val requestedPath = HttpRequestTools.queryParam(path, "path") ?: "/sdcard"
        val validation = RemotePathGuard.validate(requestedPath)
        if (validation != null) return HttpResponseFactory.forbidden(validation)
        if (!authorized(path)) return HttpResponseFactory.unauthorized()
        auditLog.record(RemoteAuditEvent(RemoteAuditEventType.ListDirectory, "Directory listed", requestedPath))
        return HttpResponseFactory.json(RemoteDirectorySerializer.serialize(fileRepository.list(requestedPath)))
    }

    private fun downloadResponse(path: String): HttpResponse {
        val requestedPath = HttpRequestTools.queryParam(path, "path") ?: return HttpResponseFactory.badRequest("Missing path")
        if (!authorized(path)) return HttpResponseFactory.unauthorized()
        val plan = downloadPlanner.plan(requestedPath).getOrElse { return HttpResponseFactory.badRequest(it.message ?: "Cannot download") }
        auditLog.record(RemoteAuditEvent(RemoteAuditEventType.DownloadRequested, "Download requested", plan.path))
        return HttpResponse.FileStream(file = File(plan.path), contentType = plan.mimeType, downloadName = plan.fileName)
    }

    private suspend fun renameResponse(path: String): HttpResponse {
        if (!authorized(path)) return HttpResponseFactory.unauthorized()
        return writeController.rename(RemoteRenameRequest(HttpRequestTools.queryParam(path, "path") ?: return HttpResponseFactory.badRequest("Missing path"), HttpRequestTools.queryParam(path, "name") ?: return HttpResponseFactory.badRequest("Missing name"), HttpRequestTools.queryParam(path, "pin")))
    }

    private suspend fun deleteResponse(path: String): HttpResponse {
        if (!authorized(path)) return HttpResponseFactory.unauthorized()
        return writeController.delete(RemoteDeleteRequest(HttpRequestTools.queryParam(path, "path") ?: return HttpResponseFactory.badRequest("Missing path"), HttpRequestTools.queryParam(path, "pin")))
    }

    private suspend fun mkdirResponse(path: String): HttpResponse {
        if (!authorized(path)) return HttpResponseFactory.unauthorized()
        return writeController.mkdir(RemoteMkdirRequest(HttpRequestTools.queryParam(path, "path") ?: return HttpResponseFactory.badRequest("Missing path"), HttpRequestTools.queryParam(path, "name") ?: return HttpResponseFactory.badRequest("Missing name"), HttpRequestTools.queryParam(path, "pin")))
    }

    private fun uploadResponse(path: String): HttpResponse {
        if (!authorized(path)) return HttpResponseFactory.unauthorized()
        return writeController.uploadReserved(RemoteUploadRequest(HttpRequestTools.queryParam(path, "path") ?: return HttpResponseFactory.badRequest("Missing path"), HttpRequestTools.queryParam(path, "name") ?: "upload.bin", HttpRequestTools.queryParam(path, "pin")))
    }
}

package com.sere.filemanager.core.remote

import com.sere.filemanager.core.files.FileRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException

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
    private val executor = RemoteRouteExecutor(fileRepository, config = config)
    private var pin: String? = null
    private var session: RemoteServerSession = RemoteServerSession.stopped()

    fun start(sessionPin: String) {
        if (job?.isActive == true) return
        pin = sessionPin
        session = RemoteServerStatusStore.current()
        job = scope.launch {
            try {
                serverSocket = ServerSocket(config.port)
                while (isActive) {
                    val socket = try { serverSocket?.accept() ?: break } catch (_: SocketException) { break }
                    launch { handle(socket) }
                }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Throwable) {
                auditLog.record(RemoteAuditEvent(RemoteAuditEventType.Error, error.message ?: "HTTP server failed"))
            } finally {
                runCatching { serverSocket?.close() }
                serverSocket = null
            }
        }
    }

    fun stop() {
        val socket = serverSocket
        serverSocket = null
        runCatching { socket?.close() }
        job?.cancel()
        job = null
        session = RemoteServerSession.stopped()
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
        val query = RemoteRouteQuery(path)
        return when (route) {
            RemoteRoutes.INDEX -> if (method == "GET") HttpResponseFactory.html(WebManagerPage.render(currentSessionForStatus())) else HttpResponseFactory.badRequest("Unsupported method")
            RemoteRoutes.API_STATUS -> if (method == "GET") HttpResponseFactory.json(executor.status(currentSessionForStatus())) else HttpResponseFactory.badRequest("Unsupported method")
            RemoteRoutes.API_LIST -> if (method == "GET") executor.list(query.path(), query.pin()).toHttpJson() else HttpResponseFactory.badRequest("Unsupported method")
            RemoteRoutes.API_DOWNLOAD -> if (method == "GET") downloadResponse(path) else HttpResponseFactory.badRequest("Unsupported method")
            RemoteRoutes.API_RENAME -> if (method == "POST") executor.rename(query.path(), query.name() ?: return HttpResponseFactory.badRequest("Missing name"), query.pin()).toHttpText() else HttpResponseFactory.badRequest("Use POST")
            RemoteRoutes.API_DELETE -> if (method == "POST") executor.delete(query.path(), query.pin()).toHttpText() else HttpResponseFactory.badRequest("Use POST")
            RemoteRoutes.API_MKDIR -> if (method == "POST") executor.mkdir(query.path(), query.name() ?: return HttpResponseFactory.badRequest("Missing name"), query.pin()).toHttpText() else HttpResponseFactory.badRequest("Use POST")
            RemoteRoutes.API_UPLOAD -> if (method == "POST") executor.validateUpload(query.path(), query.name() ?: "upload.bin", null, query.pin()).toHttpText() else HttpResponseFactory.badRequest("Use POST")
            else -> HttpResponseFactory.notFound()
        }
    }

    private fun authorized(path: String): Boolean = !config.requirePin || auth.isPinValid(pin, HttpRequestTools.queryParam(path, "pin"))

    private fun downloadResponse(path: String): HttpResponse {
        val requestedPath = HttpRequestTools.queryParam(path, "path") ?: return HttpResponseFactory.badRequest("Missing path")
        if (!authorized(path)) return HttpResponseFactory.unauthorized()
        val plan = downloadPlanner.plan(requestedPath).getOrElse { return HttpResponseFactory.badRequest(it.message ?: "Cannot download") }
        auditLog.record(RemoteAuditEvent(RemoteAuditEventType.DownloadRequested, "Download requested", plan.path))
        return HttpResponse.FileStream(file = File(plan.path), contentType = plan.mimeType, downloadName = plan.fileName)
    }

    private fun currentSessionForStatus(): RemoteServerSession = RemoteServerStatusStore.current().let { if (it.state.name == "Stopped") session else it }

    private fun RemoteExecutionResult.toHttpJson(): HttpResponse = if (success) HttpResponseFactory.json(body) else HttpResponse.Text(statusCode, body, "text/plain; charset=utf-8")
    private fun RemoteExecutionResult.toHttpText(): HttpResponse = HttpResponse.Text(statusCode, body, "text/plain; charset=utf-8")
}

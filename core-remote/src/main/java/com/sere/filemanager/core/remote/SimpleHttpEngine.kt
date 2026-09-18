package com.sere.filemanager.core.remote

import com.sere.filemanager.core.files.FileRepository
import com.sere.filemanager.core.model.RemoteSession
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
    private val auditSink = InMemoryRemoteAuditSink()
    private val auditRoutes = RemoteAuditRouteController(auditSink)
    private val executor = RemoteRouteExecutor(fileRepository, config = config, audit = auditSink)
    private val uploadWriter = RemoteUploadWriter()
    private val rateLimiter = RemoteRateLimiter()
    private val routePolicies = RemoteRoutePolicyResolver()
    private var pin: String? = null
    private var session: RemoteSession = RemoteSession.stopped()

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
            } catch (error: CancellationException) { throw error }
            catch (error: Throwable) { auditLog.record(RemoteAuditEvent(RemoteAuditEventType.Error, error.message ?: "HTTP server failed")) }
            finally { runCatching { serverSocket?.close() }; serverSocket = null }
        }
    }

    fun stop() { val socket = serverSocket; serverSocket = null; runCatching { socket?.close() }; job?.cancel(); job = null; session = RemoteSession.stopped() }

    private suspend fun handle(socket: Socket) {
        socket.use { client ->
            val output = client.getOutputStream()
            val clientKey = client.inetAddress?.hostAddress ?: "unknown"
            if (!rateLimiter.allow(clientKey)) {
                auditSink.record(RemoteAuditEntry(action = RemoteAuditAction.Denied, path = "/", success = false, message = "Rate limit", client = clientKey))
                HttpResponseWriter.write(output, HttpResponse.Text("429 Too Many Requests", "text/plain; charset=utf-8", "Too many requests")); output.flush(); return
            }
            val request = runCatching { HttpRawRequestParser.parse(client.getInputStream()) }.getOrElse {
                HttpResponseWriter.write(output, HttpResponseFactory.badRequest(it.message ?: "Bad request")); output.flush(); return
            }
            val response = runCatching { route(request, clientKey) }.getOrElse {
                auditLog.record(RemoteAuditEvent(RemoteAuditEventType.Error, it.message ?: "Server error"))
                auditSink.record(RemoteAuditEntry(action = RemoteAuditAction.Denied, path = request.path, success = false, message = it.message ?: "Server error", client = clientKey))
                HttpResponseFactory.serverError(it.message ?: "Server error")
            }
            HttpResponseWriter.write(output, response)
            output.flush()
        }
    }

    private suspend fun route(request: HttpRawRequest, clientKey: String): HttpResponse {
        val route = HttpRequestTools.routePath(request.path)
        val query = RemoteRouteQuery(request.path)
        val policyFailure = validateRoutePolicy(route, query.pin(), clientKey)
        if (policyFailure != null) return policyFailure
        return when (route) {
            RemoteRoutes.INDEX -> if (request.method == "GET") HttpResponseFactory.html(WebManagerPage.render(currentSessionForStatus(), config)) else HttpResponseFactory.badRequest("Unsupported method")
            RemoteRoutes.API_STATUS -> if (request.method == "GET") HttpResponseFactory.json(executor.status(currentSessionForStatus())) else HttpResponseFactory.badRequest("Unsupported method")
            RemoteRoutes.API_AUDIT -> if (request.method == "GET") guardedAuditJson(query.pin()) else HttpResponseFactory.badRequest("Unsupported method")
            RemoteRoutes.API_AUDIT_EXPORT -> if (request.method == "GET") guardedAuditExport(query.pin()) else HttpResponseFactory.badRequest("Unsupported method")
            RemoteRoutes.API_LIST -> if (request.method == "GET") executor.list(query.path(), query.pin()).toHttpJson() else HttpResponseFactory.badRequest("Unsupported method")
            RemoteRoutes.API_DOWNLOAD -> if (request.method == "GET") downloadResponse(request.path, clientKey) else HttpResponseFactory.badRequest("Unsupported method")
            RemoteRoutes.API_RENAME -> if (request.method == "POST") executor.rename(query.path(), query.name() ?: return HttpResponseFactory.badRequest("Missing name"), query.pin()).toHttpText() else HttpResponseFactory.badRequest("Use POST")
            RemoteRoutes.API_DELETE -> if (request.method == "POST") executor.delete(query.path(), query.pin()).toHttpText() else HttpResponseFactory.badRequest("Use POST")
            RemoteRoutes.API_MKDIR -> if (request.method == "POST") executor.mkdir(query.path(), query.name() ?: return HttpResponseFactory.badRequest("Missing name"), query.pin()).toHttpText() else HttpResponseFactory.badRequest("Use POST")
            RemoteRoutes.API_UPLOAD -> if (request.method == "POST") uploadResponse(request, query, clientKey) else HttpResponseFactory.badRequest("Use POST")
            else -> HttpResponseFactory.notFound()
        }
    }

    private fun validateRoutePolicy(route: String, pinParam: String?, clientKey: String): HttpResponse? {
        if (route == RemoteRoutes.INDEX) return null
        val validation = routePolicies.validateRoute(route, config, pinParam)
        if (!validation.allowed) {
            auditSink.record(RemoteAuditEntry(action = RemoteAuditAction.Denied, path = route, success = false, message = validation.message, client = clientKey))
            return if (validation.message == RemoteWebMessages.invalidPin) HttpResponseFactory.unauthorized() else HttpResponseFactory.forbidden(validation.message ?: "Denied")
        }
        if (config.requirePin && !auth.isPinValid(pin, pinParam)) {
            auditSink.record(RemoteAuditEntry(action = RemoteAuditAction.Denied, path = route, success = false, message = RemoteWebMessages.invalidPin, client = clientKey))
            return HttpResponseFactory.unauthorized()
        }
        return null
    }

    private fun guardedAuditJson(pinParam: String?): HttpResponse = if (!config.requirePin || auth.isPinValid(pin, pinParam)) HttpResponseFactory.json(auditRoutes.latestJson()) else HttpResponseFactory.unauthorized()
    private fun guardedAuditExport(pinParam: String?): HttpResponse = if (!config.requirePin || auth.isPinValid(pin, pinParam)) HttpResponse.Text("200 OK", "text/plain; charset=utf-8", auditRoutes.exportText()) else HttpResponseFactory.unauthorized()
    private fun authorized(path: String): Boolean = !config.requirePin || auth.isPinValid(pin, HttpRequestTools.queryParam(path, "pin"))

    private fun downloadResponse(path: String, clientKey: String): HttpResponse {
        val requestedPath = HttpRequestTools.queryParam(path, "path") ?: return HttpResponseFactory.badRequest("Missing path")
        if (!authorized(path)) return HttpResponseFactory.unauthorized()
        val plan = downloadPlanner.plan(requestedPath).getOrElse { error ->
            val message = error.message ?: "Cannot download"
            auditSink.record(RemoteAuditEntry(action = RemoteAuditAction.Download, path = requestedPath, success = false, message = message, client = clientKey))
            return when {
                message.contains("does not exist", ignoreCase = true) -> HttpResponseFactory.notFound(message)
                message.contains("not a file", ignoreCase = true) -> HttpResponseFactory.badRequest(message)
                message.contains("Invalid", ignoreCase = true) -> HttpResponseFactory.forbidden(message)
                else -> HttpResponseFactory.badRequest(message)
            }
        }
        auditLog.record(RemoteAuditEvent(RemoteAuditEventType.DownloadRequested, "Download requested", plan.path))
        auditSink.record(RemoteAuditEntry(action = RemoteAuditAction.Download, path = plan.path, success = true, message = "Download requested", client = clientKey))
        return HttpResponse.FileStream(file = File(plan.path), contentType = plan.mimeType, downloadName = plan.fileName)
    }

    private fun uploadResponse(request: HttpRawRequest, query: RemoteRouteQuery, clientKey: String): HttpResponse {
        val directory = query.path()
        val name = query.name() ?: "upload.bin"
        val validation = executor.validateUpload(directory, name, request.contentLength(), query.pin())
        if (!validation.success) return validation.toHttpText()
        val result = uploadWriter.writeRawRequest(directory, name, request)
        auditSink.record(RemoteAuditEntry(action = RemoteAuditAction.Upload, path = directory, success = result.success, message = result.body, client = clientKey))
        return result.toHttpText()
    }

    private fun currentSessionForStatus(): RemoteSession = RemoteServerStatusStore.current().let { if (it.state.name == "Stopped") session else it }
    private fun RemoteExecutionResult.toHttpJson(): HttpResponse = if (success) HttpResponseFactory.json(body) else HttpResponse.Text(statusLine(statusCode), "text/plain; charset=utf-8", body)
    private fun RemoteExecutionResult.toHttpText(): HttpResponse = HttpResponse.Text(statusLine(statusCode), "text/plain; charset=utf-8", body)
    private fun statusLine(code: Int): String = when (code) {
        200 -> "200 OK"
        400 -> "400 Bad Request"
        403 -> "403 Forbidden"
        404 -> "404 Not Found"
        429 -> "429 Too Many Requests"
        500 -> "500 Internal Server Error"
        else -> "$code"
    }
}

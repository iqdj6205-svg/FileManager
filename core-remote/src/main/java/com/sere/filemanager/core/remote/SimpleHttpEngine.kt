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
    private val uploadWriter = RemoteUploadWriter()
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
            } catch (error: CancellationException) { throw error }
            catch (error: Throwable) { auditLog.record(RemoteAuditEvent(RemoteAuditEventType.Error, error.message ?: "HTTP server failed")) }
            finally { runCatching { serverSocket?.close() }; serverSocket = null }
        }
    }

    fun stop() { val socket = serverSocket; serverSocket = null; runCatching { socket?.close() }; job?.cancel(); job = null; session = RemoteServerSession.stopped() }

    private suspend fun handle(socket: Socket) {
        socket.use { client ->
            val output = client.getOutputStream()
            val request = runCatching { HttpRawRequestParser.parse(client.getInputStream()) }.getOrElse {
                HttpResponseWriter.write(output, HttpResponseFactory.badRequest(it.message ?: "Bad request")); output.flush(); return
            }
            val response = runCatching { route(request) }.getOrElse {
                auditLog.record(RemoteAuditEvent(RemoteAuditEventType.Error, it.message ?: "Server error"))
                HttpResponseFactory.serverError(it.message ?: "Server error")
            }
            HttpResponseWriter.write(output, response)
            output.flush()
        }
    }

    private suspend fun route(request: HttpRawRequest): HttpResponse {
        val route = HttpRequestTools.routePath(request.path)
        val query = RemoteRouteQuery(request.path)
        return when (route) {
            RemoteRoutes.INDEX -> if (request.method == "GET") HttpResponseFactory.html(WebManagerPage.render(currentSessionForStatus())) else HttpResponseFactory.badRequest("Unsupported method")
            RemoteRoutes.API_STATUS -> if (request.method == "GET") HttpResponseFactory.json(executor.status(currentSessionForStatus())) else HttpResponseFactory.badRequest("Unsupported method")
            RemoteRoutes.API_LIST -> if (request.method == "GET") executor.list(query.path(), query.pin()).toHttpJson() else HttpResponseFactory.badRequest("Unsupported method")
            RemoteRoutes.API_DOWNLOAD -> if (request.method == "GET") downloadResponse(request.path) else HttpResponseFactory.badRequest("Unsupported method")
            RemoteRoutes.API_RENAME -> if (request.method == "POST") executor.rename(query.path(), query.name() ?: return HttpResponseFactory.badRequest("Missing name"), query.pin()).toHttpText() else HttpResponseFactory.badRequest("Use POST")
            RemoteRoutes.API_DELETE -> if (request.method == "POST") executor.delete(query.path(), query.pin()).toHttpText() else HttpResponseFactory.badRequest("Use POST")
            RemoteRoutes.API_MKDIR -> if (request.method == "POST") executor.mkdir(query.path(), query.name() ?: return HttpResponseFactory.badRequest("Missing name"), query.pin()).toHttpText() else HttpResponseFactory.badRequest("Use POST")
            RemoteRoutes.API_UPLOAD -> if (request.method == "POST") uploadResponse(request, query) else HttpResponseFactory.badRequest("Use POST")
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

    private fun uploadResponse(request: HttpRawRequest, query: RemoteRouteQuery): HttpResponse {
        val directory = query.path()
        val name = query.name() ?: "upload.bin"
        val validation = executor.validateUpload(directory, name, request.contentLength(), query.pin())
        if (!validation.success) return validation.toHttpText()
        return uploadWriter.writeRawRequest(directory, name, request).toHttpText()
    }

    private fun currentSessionForStatus(): RemoteServerSession = RemoteServerStatusStore.current().let { if (it.state.name == "Stopped") session else it }
    private fun RemoteExecutionResult.toHttpJson(): HttpResponse = if (success) HttpResponseFactory.json(body) else HttpResponse.Text(statusCode, body, "text/plain; charset=utf-8")
    private fun RemoteExecutionResult.toHttpText(): HttpResponse = HttpResponse.Text(statusCode, body, "text/plain; charset=utf-8")
}

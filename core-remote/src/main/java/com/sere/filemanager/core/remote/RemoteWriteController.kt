package com.sere.filemanager.core.remote

import com.sere.filemanager.core.files.FileOperation
import com.sere.filemanager.core.files.SafeFileOperations

class RemoteWriteController(
    private val operations: SafeFileOperations,
    private val config: RemoteConfig,
    private val auditLog: RemoteAuditLog = InMemoryRemoteAuditLog(),
) {
    suspend fun rename(request: RemoteRenameRequest): HttpResponse {
        if (!config.allowDelete) return HttpResponseFactory.forbidden("Remote write actions are disabled")
        val validation = RemotePathGuard.validate(request.path)
        if (validation != null) return HttpResponseFactory.forbidden(validation)
        val result = operations.execute(FileOperation.Rename(request.path, request.newName))
        auditLog.record(RemoteAuditEvent(RemoteAuditEventType.DeleteRequested, "Remote rename requested", request.path))
        return if (result.success) HttpResponseFactory.json("{\"ok\":true}") else HttpResponseFactory.badRequest(result.message ?: "Rename failed")
    }

    suspend fun delete(request: RemoteDeleteRequest): HttpResponse {
        if (!config.allowDelete) return HttpResponseFactory.forbidden("Remote delete is disabled")
        val validation = RemotePathGuard.validate(request.path)
        if (validation != null) return HttpResponseFactory.forbidden(validation)
        val result = operations.execute(FileOperation.Delete(request.path))
        auditLog.record(RemoteAuditEvent(RemoteAuditEventType.DeleteRequested, "Remote delete requested", request.path))
        return if (result.success) HttpResponseFactory.json("{\"ok\":true}") else HttpResponseFactory.badRequest(result.message ?: "Delete failed")
    }

    suspend fun mkdir(request: RemoteMkdirRequest): HttpResponse {
        if (!config.allowUploads) return HttpResponseFactory.forbidden("Remote write actions are disabled")
        val validation = RemotePathGuard.validate(request.parentPath)
        if (validation != null) return HttpResponseFactory.forbidden(validation)
        val result = operations.execute(FileOperation.CreateFolder(request.parentPath, request.name))
        return if (result.success) HttpResponseFactory.json("{\"ok\":true}") else HttpResponseFactory.badRequest(result.message ?: "Create folder failed")
    }

    fun uploadReserved(request: RemoteUploadRequest): HttpResponse {
        if (!config.allowUploads) return HttpResponseFactory.forbidden("Remote uploads are disabled")
        auditLog.record(RemoteAuditEvent(RemoteAuditEventType.UploadRequested, "Remote upload requested", request.targetPath))
        return HttpResponseFactory.text("Upload parser reserved for ${request.fileName}")
    }
}

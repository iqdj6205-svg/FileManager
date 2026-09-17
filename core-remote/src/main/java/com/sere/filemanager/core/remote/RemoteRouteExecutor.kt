package com.sere.filemanager.core.remote

import com.sere.filemanager.core.files.FileOperation
import com.sere.filemanager.core.files.FileRepository
import com.sere.filemanager.core.files.SafeFileOperations

class RemoteRouteExecutor(
    private val repository: FileRepository,
    private val operations: SafeFileOperations = SafeFileOperations(repository),
    private val config: RemoteConfig = RemoteConfig(),
    private val audit: RemoteAuditSink = InMemoryRemoteAuditSink(),
    private val validator: RemoteRequestValidator = RemoteRequestValidator(config),
    private val uploadPolicy: RemoteUploadPolicy = RemoteUploadPolicy(),
) {
    fun status(session: RemoteServerSession): String {
        audit.record(RemoteAuditEntry(action = RemoteAuditAction.Status, path = "/", success = true))
        return RemoteStatusJson.render(session, RemoteAuditSummarizer().summarize(audit.latest()))
    }

    suspend fun list(path: String, pin: String?): RemoteExecutionResult {
        val validation = validator.validateRead(path, pin)
        if (!validation.allowed) return denied(RemoteAuditAction.List, path, validation.message)
        return runCatching {
            val items = repository.list(path)
            audit.record(RemoteAuditEntry(action = RemoteAuditAction.List, path = path, success = true))
            RemoteExecutionResult.ok(RemoteDirectorySerializer.toJson(path, items))
        }.getOrElse { error -> failed(RemoteAuditAction.List, path, error.message) }
    }

    suspend fun mkdir(path: String, name: String, pin: String?): RemoteExecutionResult {
        val validation = validator.validateUpload(path, pin)
        if (!validation.allowed) return denied(RemoteAuditAction.Mkdir, path, validation.message)
        val result = operations.execute(FileOperation.CreateFolder(path, name))
        audit.record(RemoteAuditEntry(action = RemoteAuditAction.Mkdir, path = path, success = result.success, message = result.message))
        return RemoteExecutionResult.fromOperation(result)
    }

    suspend fun rename(path: String, name: String, pin: String?): RemoteExecutionResult {
        val validation = validator.validateDestructive(path, pin)
        if (!validation.allowed) return denied(RemoteAuditAction.Rename, path, validation.message)
        val result = operations.execute(FileOperation.Rename(path, name))
        audit.record(RemoteAuditEntry(action = RemoteAuditAction.Rename, path = path, success = result.success, message = result.message))
        return RemoteExecutionResult.fromOperation(result)
    }

    suspend fun delete(path: String, pin: String?): RemoteExecutionResult {
        val validation = validator.validateDestructive(path, pin)
        if (!validation.allowed) return denied(RemoteAuditAction.Delete, path, validation.message)
        val result = operations.execute(FileOperation.Delete(path))
        audit.record(RemoteAuditEntry(action = RemoteAuditAction.Delete, path = path, success = result.success, message = result.message))
        return RemoteExecutionResult.fromOperation(result)
    }

    fun validateUpload(path: String, fileName: String, sizeBytes: Long?, pin: String?): RemoteExecutionResult {
        val route = validator.validateUpload(path, pin)
        if (!route.allowed) return denied(RemoteAuditAction.Upload, path, route.message)
        val upload = uploadPolicy.validate(fileName, sizeBytes)
        if (!upload.allowed) return denied(RemoteAuditAction.Upload, path, upload.message)
        audit.record(RemoteAuditEntry(action = RemoteAuditAction.Upload, path = path, success = true, message = "Upload accepted"))
        return RemoteExecutionResult.ok("Upload accepted")
    }

    private fun denied(action: RemoteAuditAction, path: String, message: String?): RemoteExecutionResult {
        audit.record(RemoteAuditEntry(action = action, path = path, success = false, message = message))
        return RemoteExecutionResult(false, 403, message ?: "Denied")
    }

    private fun failed(action: RemoteAuditAction, path: String, message: String?): RemoteExecutionResult {
        audit.record(RemoteAuditEntry(action = action, path = path, success = false, message = message))
        return RemoteExecutionResult(false, 500, message ?: "Failed")
    }
}

data class RemoteExecutionResult(
    val success: Boolean,
    val statusCode: Int,
    val body: String,
) {
    companion object {
        fun ok(body: String) = RemoteExecutionResult(true, 200, body)
        fun fromOperation(result: com.sere.filemanager.core.files.FileOperationResult): RemoteExecutionResult =
            RemoteExecutionResult(result.success, if (result.success) 200 else 400, result.message ?: if (result.success) "OK" else "Failed")
    }
}

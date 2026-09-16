package com.sere.filemanager.core.remote

enum class RemoteAuditEventType { ServerStarted, ServerStopped, ListDirectory, DownloadRequested, UploadRequested, DeleteRequested, Error }

data class RemoteAuditEvent(
    val type: RemoteAuditEventType,
    val message: String,
    val path: String? = null,
    val timestampMillis: Long = System.currentTimeMillis(),
)

interface RemoteAuditLog {
    fun record(event: RemoteAuditEvent)
    fun list(): List<RemoteAuditEvent>
    fun clear()
}

class InMemoryRemoteAuditLog : RemoteAuditLog {
    private val events = mutableListOf<RemoteAuditEvent>()
    override fun record(event: RemoteAuditEvent) { events.add(0, event) }
    override fun list(): List<RemoteAuditEvent> = events.toList()
    override fun clear() { events.clear() }
}

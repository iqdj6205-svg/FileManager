package com.sere.filemanager.core.remote

data class RemoteAuditEntry(
    val atMillis: Long = System.currentTimeMillis(),
    val action: RemoteAuditAction,
    val path: String,
    val success: Boolean,
    val message: String? = null,
    val client: String? = null,
)

enum class RemoteAuditAction { List, Download, Upload, Rename, Delete, Mkdir, Status, Denied }

interface RemoteAuditSink {
    fun record(entry: RemoteAuditEntry)
    fun latest(limit: Int = 50): List<RemoteAuditEntry>
}

class InMemoryRemoteAuditSink : RemoteAuditSink {
    private val entries = ArrayDeque<RemoteAuditEntry>()
    override fun record(entry: RemoteAuditEntry) {
        synchronized(entries) {
            entries.addFirst(entry)
            while (entries.size > 200) entries.removeLast()
        }
    }
    override fun latest(limit: Int): List<RemoteAuditEntry> = synchronized(entries) { entries.take(limit.coerceIn(1, 200)) }
}

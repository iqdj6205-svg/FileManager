package com.sere.filemanager.core.remote

data class RemoteAuditSummary(
    val total: Int,
    val successful: Int,
    val failed: Int,
    val destructiveActions: Int,
    val uploads: Int,
    val downloads: Int,
)

class RemoteAuditSummarizer {
    fun summarize(entries: List<RemoteAuditEntry>): RemoteAuditSummary = RemoteAuditSummary(
        total = entries.size,
        successful = entries.count { it.success },
        failed = entries.count { !it.success },
        destructiveActions = entries.count { it.action == RemoteAuditAction.Delete || it.action == RemoteAuditAction.Rename },
        uploads = entries.count { it.action == RemoteAuditAction.Upload },
        downloads = entries.count { it.action == RemoteAuditAction.Download },
    )
}

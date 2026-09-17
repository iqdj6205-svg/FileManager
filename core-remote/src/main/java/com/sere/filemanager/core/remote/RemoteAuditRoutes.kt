package com.sere.filemanager.core.remote

object RemoteAuditRoutes {
    const val API_AUDIT = "/api/audit"
    const val API_AUDIT_EXPORT = "/api/audit/export"
}

class RemoteAuditRouteController(
    private val audit: RemoteAuditSink,
    private val exporter: RemoteAuditExporter = RemoteAuditExporter(),
    private val summarizer: RemoteAuditSummarizer = RemoteAuditSummarizer(),
) {
    fun latestJson(limit: Int = 50): String {
        val entries = audit.latest(limit)
        val summary = summarizer.summarize(entries)
        return buildString {
            append("{\"summary\":{")
            append("\"total\":${summary.total},")
            append("\"success\":${summary.successful},")
            append("\"failed\":${summary.failed}")
            append("},\"entries\":[")
            entries.forEachIndexed { index, entry ->
                if (index > 0) append(',')
                append("{")
                append("\"at\":${entry.atMillis},")
                append("\"action\":\"${entry.action.name}\",")
                append("\"path\":\"${entry.path.escape()}\",")
                append("\"success\":${entry.success},")
                append("\"message\":\"${entry.message.orEmpty().escape()}\"")
                append("}")
            }
            append("]}")
        }
    }

    fun exportText(limit: Int = 200): String = exporter.toText(audit.latest(limit))

    fun exportJson(limit: Int = 200): String = exporter.toJson(audit.latest(limit))

    private fun String.escape(): String = replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
}

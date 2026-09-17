package com.sere.filemanager.core.remote

class RemoteAuditExporter {
    fun toJson(entries: List<RemoteAuditEntry>): String = buildString {
        append("[")
        entries.forEachIndexed { index, e ->
            if (index > 0) append(',')
            append('{')
            append("\"atMillis\":").append(e.atMillis).append(',')
            append("\"action\":\"").append(e.action.name).append("\",")
            append("\"path\":\"").append(escape(e.path)).append("\",")
            append("\"success\":").append(e.success)
            e.message?.let { append(",\"message\":\"").append(escape(it)).append("\"") }
            e.client?.let { append(",\"client\":\"").append(escape(it)).append("\"") }
            append('}')
        }
        append("]")
    }

    private fun escape(value: String): String = value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
}

package com.sere.filemanager.core.remote

object RemoteStatusJson {
    fun render(session: RemoteServerSession, audit: RemoteAuditSummary? = null, health: RemoteServerHealth? = null): String = buildString {
        append('{')
        append("\"state\":\"").append(session.state.name).append("\",")
        append("\"url\":").append(session.url?.let { "\"${escape(it)}\"" } ?: "null").append(',')
        append("\"pinRequired\":").append(session.pin != null)
        session.pin?.let { append(",\"pin\":\"").append(escape(it)).append("\"") }
        audit?.let { append(",\"audit\":{").append("\"total\":").append(it.total).append(",\"successful\":").append(it.successful).append(",\"failed\":").append(it.failed).append(",\"uploads\":").append(it.uploads).append(",\"downloads\":").append(it.downloads).append(",\"destructiveActions\":").append(it.destructiveActions).append('}') }
        health?.let { append(",\"health\":{").append("\"networkAvailable\":").append(it.networkAvailable).append(",\"localAddress\":").append(it.localAddress?.let { a -> "\"${escape(a)}\"" } ?: "null").append(",\"batteryPercent\":").append(it.batteryPercent ?: "null").append('}') }
        append('}')
    }

    private fun escape(value: String): String = value.replace("\\", "\\\\").replace("\"", "\\\"")
}

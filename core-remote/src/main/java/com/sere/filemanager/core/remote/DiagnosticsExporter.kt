package com.sere.filemanager.core.remote

import com.sere.filemanager.core.model.DiagnosticsReport

object DiagnosticsExporter {
    fun toText(report: DiagnosticsReport): String = buildString {
        appendLine("FileManager diagnostics")
        appendLine("Generated: ${report.generatedAtMillis}")
        report.entries.forEach { entry ->
            appendLine("[${entry.severity}] ${entry.title}: ${entry.value}")
        }
    }
}

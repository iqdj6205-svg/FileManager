package com.sere.filemanager.core.model

data class DiagnosticEntry(
    val title: String,
    val value: String,
    val severity: DiagnosticSeverity = DiagnosticSeverity.Info,
)

enum class DiagnosticSeverity { Info, Warning, Error }

data class DiagnosticsReport(
    val generatedAtMillis: Long,
    val entries: List<DiagnosticEntry>,
)

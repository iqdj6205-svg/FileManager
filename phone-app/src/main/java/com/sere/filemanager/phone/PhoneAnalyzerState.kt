package com.sere.filemanager.phone

import androidx.compose.runtime.Immutable
import com.sere.filemanager.core.files.StorageAnalysis
import com.sere.filemanager.core.model.StorageInsight

@Immutable
data class PhoneAnalyzerState(
    val analysis: StorageAnalysis? = null,
    val insights: List<StorageInsight> = emptyList(),
    val isLoading: Boolean = false,
    val message: String? = null,
)

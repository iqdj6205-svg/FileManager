package com.sere.filemanager.core.files

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class FileOperationProgress(
    val operationLabel: String = "Idle",
    val source: String? = null,
    val target: String? = null,
    val bytesDone: Long = 0L,
    val bytesTotal: Long? = null,
    val completed: Boolean = false,
    val failed: Boolean = false,
    val message: String? = null,
) {
    val percent: Int? get() = bytesTotal?.takeIf { it > 0L }?.let { ((bytesDone * 100L) / it).toInt().coerceIn(0, 100) }
}

interface FileOperationProgressSink {
    val progress: StateFlow<FileOperationProgress>
    fun update(progress: FileOperationProgress)
    fun reset()
}

class InMemoryFileOperationProgressSink : FileOperationProgressSink {
    private val _progress = MutableStateFlow(FileOperationProgress())
    override val progress: StateFlow<FileOperationProgress> = _progress
    override fun update(progress: FileOperationProgress) { _progress.value = progress }
    override fun reset() { _progress.value = FileOperationProgress() }
}

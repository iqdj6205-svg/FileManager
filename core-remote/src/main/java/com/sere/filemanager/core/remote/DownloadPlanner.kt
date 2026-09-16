package com.sere.filemanager.core.remote

import java.io.File

class DownloadPlanner {
    fun plan(path: String): Result<DownloadPlan> = runCatching {
        val validation = RemotePathGuard.validate(path)
        require(validation == null) { validation ?: "Invalid path" }
        val file = File(path)
        require(file.exists()) { "File does not exist" }
        require(file.isFile) { "Path is not a file" }
        DownloadPlan(
            path = file.absolutePath,
            fileName = file.name,
            mimeType = MimeTypes.fromFileName(file.name),
            sizeBytes = file.length(),
        )
    }
}

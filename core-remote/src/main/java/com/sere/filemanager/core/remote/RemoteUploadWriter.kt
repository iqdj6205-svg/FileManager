package com.sere.filemanager.core.remote

import com.sere.filemanager.core.files.PathTools
import java.io.File
import java.io.InputStream

class RemoteUploadWriter(
    private val policy: RemoteUploadPolicy = RemoteUploadPolicy(),
) {
    fun targetFile(directoryPath: String, fileName: String): File {
        val safeName = fileName.trim().replace('/', '_').replace('\\', '_').ifBlank { "upload.bin" }
        return File(PathTools.child(directoryPath, safeName))
    }

    fun write(directoryPath: String, fileName: String, sizeBytes: Long?, input: InputStream): RemoteExecutionResult {
        val validation = policy.validate(fileName, sizeBytes)
        if (!validation.allowed) return RemoteExecutionResult(false, 400, validation.message ?: "Upload blocked")
        val target = targetFile(directoryPath, fileName)
        target.parentFile?.mkdirs()
        var written = 0L
        target.outputStream().use { out ->
            val buffer = ByteArray(32 * 1024)
            while (true) {
                val read = input.read(buffer)
                if (read <= 0) break
                written += read
                if (written > policy.maxBytes) {
                    runCatching { target.delete() }
                    return RemoteExecutionResult(false, 413, "Upload is larger than limit")
                }
                out.write(buffer, 0, read)
            }
        }
        return RemoteExecutionResult.ok("Uploaded ${target.name} ($written bytes)")
    }
}

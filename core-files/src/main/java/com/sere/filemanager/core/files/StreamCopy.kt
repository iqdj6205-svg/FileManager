package com.sere.filemanager.core.files

import java.io.InputStream
import java.io.OutputStream

class StreamCopy(
    private val bufferSize: Int = 64 * 1024,
) {
    fun copy(
        input: InputStream,
        output: OutputStream,
        totalBytes: Long? = null,
        onProgress: (Long, Long?) -> Unit = { _, _ -> },
    ): Long {
        val buffer = ByteArray(bufferSize)
        var copied = 0L
        while (true) {
            val read = input.read(buffer)
            if (read <= 0) break
            output.write(buffer, 0, read)
            copied += read
            onProgress(copied, totalBytes)
        }
        output.flush()
        return copied
    }
}

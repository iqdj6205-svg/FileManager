package com.sere.filemanager.core.remote

import java.io.OutputStream

object HttpResponseWriter {
    fun write(output: OutputStream, response: HttpResponse) {
        when (response) {
            is HttpResponse.Text -> writeText(output, response)
            is HttpResponse.FileStream -> writeFile(output, response)
        }
    }

    private fun writeText(output: OutputStream, response: HttpResponse.Text) {
        val bytes = response.body.toByteArray()
        output.write("HTTP/1.1 ${response.status}\r\n".toByteArray())
        output.write("Content-Type: ${response.contentType}\r\n".toByteArray())
        output.write("Content-Length: ${bytes.size}\r\n".toByteArray())
        output.write("Connection: close\r\n\r\n".toByteArray())
        output.write(bytes)
    }

    private fun writeFile(output: OutputStream, response: HttpResponse.FileStream) {
        val file = response.file
        output.write("HTTP/1.1 ${response.status}\r\n".toByteArray())
        output.write("Content-Type: ${response.contentType}\r\n".toByteArray())
        output.write("Content-Length: ${file.length()}\r\n".toByteArray())
        output.write("Content-Disposition: attachment; filename=\"${response.downloadName.replace("\"", "")}\"\r\n".toByteArray())
        output.write("Connection: close\r\n\r\n".toByteArray())
        file.inputStream().use { input -> input.copyTo(output, bufferSize = 16 * 1024) }
    }
}

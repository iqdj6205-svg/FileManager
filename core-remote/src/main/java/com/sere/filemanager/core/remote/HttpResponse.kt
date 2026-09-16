package com.sere.filemanager.core.remote

import java.io.File

sealed interface HttpResponse {
    data class Text(
        val status: String,
        val contentType: String,
        val body: String,
    ) : HttpResponse

    data class FileStream(
        val status: String = "200 OK",
        val file: File,
        val contentType: String,
        val downloadName: String = file.name,
    ) : HttpResponse
}

object HttpResponseFactory {
    fun html(body: String): HttpResponse = HttpResponse.Text("200 OK", "text/html; charset=utf-8", body)
    fun json(body: String): HttpResponse = HttpResponse.Text("200 OK", "application/json; charset=utf-8", body)
    fun text(body: String): HttpResponse = HttpResponse.Text("200 OK", "text/plain; charset=utf-8", body)
    fun badRequest(message: String): HttpResponse = HttpResponse.Text("400 Bad Request", "text/plain; charset=utf-8", message)
    fun unauthorized(): HttpResponse = HttpResponse.Text("401 Unauthorized", "text/plain; charset=utf-8", "Unauthorized")
    fun forbidden(message: String = "Forbidden"): HttpResponse = HttpResponse.Text("403 Forbidden", "text/plain; charset=utf-8", message)
    fun notFound(): HttpResponse = HttpResponse.Text("404 Not Found", "text/plain; charset=utf-8", "Not Found")
    fun serverError(message: String): HttpResponse = HttpResponse.Text("500 Internal Server Error", "text/plain; charset=utf-8", message)
}

package com.sere.filemanager.core.remote

object HttpResponses {
    fun html(body: String): String = response("200 OK", "text/html; charset=utf-8", body)
    fun json(body: String): String = response("200 OK", "application/json; charset=utf-8", body)
    fun text(body: String): String = response("200 OK", "text/plain; charset=utf-8", body)
    fun badRequest(message: String): String = response("400 Bad Request", "text/plain; charset=utf-8", message)
    fun unauthorized(): String = response("401 Unauthorized", "text/plain; charset=utf-8", "Unauthorized")
    fun forbidden(message: String = "Forbidden"): String = response("403 Forbidden", "text/plain; charset=utf-8", message)
    fun notFound(): String = response("404 Not Found", "text/plain; charset=utf-8", "Not Found")
    fun serverError(message: String): String = response("500 Internal Server Error", "text/plain; charset=utf-8", message)

    private fun response(status: String, contentType: String, body: String): String {
        val bytes = body.toByteArray()
        return "HTTP/1.1 $status\r\nContent-Type: $contentType\r\nContent-Length: ${bytes.size}\r\nConnection: close\r\n\r\n$body"
    }
}

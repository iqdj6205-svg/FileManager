package com.sere.filemanager.core.remote

import java.net.URLDecoder
import java.nio.charset.StandardCharsets

object HttpRequestTools {
    fun queryParam(path: String, name: String): String? {
        val query = path.substringAfter('?', missingDelimiterValue = "")
        if (query.isBlank()) return null
        return query.split('&')
            .mapNotNull { part ->
                val key = part.substringBefore('=', missingDelimiterValue = part)
                val value = part.substringAfter('=', missingDelimiterValue = "")
                if (key == name) decode(value) else null
            }
            .firstOrNull()
    }

    fun routePath(path: String): String = path.substringBefore('?')

    fun decode(value: String): String = URLDecoder.decode(value, StandardCharsets.UTF_8.name())
}

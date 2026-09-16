package com.sere.filemanager.core.files

object PathTools {
    fun parent(path: String): String {
        val clean = path.trimEnd('/')
        if (clean.isBlank() || clean == "/") return "/"
        return clean.substringBeforeLast('/', missingDelimiterValue = "/").ifBlank { "/" }
    }

    fun fileName(path: String): String = path.trimEnd('/').substringAfterLast('/')

    fun child(parent: String, name: String): String = parent.trimEnd('/') + "/" + name.trimStart('/')
}

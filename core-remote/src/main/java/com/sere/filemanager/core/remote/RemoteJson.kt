package com.sere.filemanager.core.remote

object RemoteJson {
    fun string(value: String): String = "\"${value.escapeJson()}\""
    fun property(name: String, value: String): String = "${string(name)}:${string(value)}"
    fun numberProperty(name: String, value: Long): String = "${string(name)}:$value"

    fun obj(vararg properties: String): String = properties.joinToString(prefix = "{", postfix = "}")
    fun array(items: List<String>): String = items.joinToString(prefix = "[", postfix = "]")

    private fun String.escapeJson(): String = replace("\\", "\\\\").replace("\"", "\\\"")
}

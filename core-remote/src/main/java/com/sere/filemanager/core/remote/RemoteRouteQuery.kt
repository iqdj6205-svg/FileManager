package com.sere.filemanager.core.remote

class RemoteRouteQuery(private val raw: String) {
    private val pairs: Map<String, String> = raw.substringAfter('?', "")
        .split('&')
        .filter { it.contains('=') }
        .associate { part ->
            val key = part.substringBefore('=')
            val value = part.substringAfter('=')
            key to decode(value)
        }

    fun value(name: String): String? = pairs[name]
    fun path(default: String = "/sdcard"): String = value("path") ?: default
    fun pin(): String? = value("pin")
    fun name(): String? = value("name")

    private fun decode(value: String): String = java.net.URLDecoder.decode(value, Charsets.UTF_8.name())
}

package com.sere.filemanager.wear

import android.content.Context
import java.net.NetworkInterface

class RemoteAddressResolver(@Suppress("UNUSED_PARAMETER") context: Context) {
    fun bestHttpUrl(port: Int = 8080): String {
        val ip = localIpAddress() ?: "127.0.0.1"
        return "http://$ip:$port"
    }

    private fun localIpAddress(): String? = runCatching {
        NetworkInterface.getNetworkInterfaces().asSequence()
            .flatMap { it.inetAddresses.asSequence() }
            .firstOrNull { !it.isLoopbackAddress && it.hostAddress?.contains(':') == false }
            ?.hostAddress
    }.getOrNull()
}

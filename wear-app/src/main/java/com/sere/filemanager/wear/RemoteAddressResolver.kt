package com.sere.filemanager.wear

import android.content.Context
import android.net.wifi.WifiManager
import java.net.NetworkInterface

class RemoteAddressResolver(private val context: Context) {
    fun bestHttpUrl(port: Int = 8080): String {
        val ip = localIpAddress() ?: wifiIpAddress() ?: "127.0.0.1"
        return "http://$ip:$port"
    }

    private fun localIpAddress(): String? = runCatching {
        NetworkInterface.getNetworkInterfaces().asSequence()
            .flatMap { it.inetAddresses.asSequence() }
            .firstOrNull { !it.isLoopbackAddress && it.hostAddress?.contains(':') == false }
            ?.hostAddress
    }.getOrNull()

    private fun wifiIpAddress(): String? = runCatching {
        val wifi = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager ?: return null
        val ip = wifi.connectionInfo.ipAddress
        if (ip == 0) null else listOf(ip and 0xff, ip shr 8 and 0xff, ip shr 16 and 0xff, ip shr 24 and 0xff).joinToString(".")
    }.getOrNull()
}

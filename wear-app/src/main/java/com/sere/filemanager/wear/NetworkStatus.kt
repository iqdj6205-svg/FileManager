package com.sere.filemanager.wear

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.net.Inet4Address
import java.net.NetworkInterface

class NetworkStatus(private val context: Context) {
    fun isConnected(): Boolean {
        val capabilities = activeCapabilities() ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun isLocalNetworkReady(): Boolean {
        val capabilities = activeCapabilities() ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) && localIpv4Address() != null
    }

    fun connectionLabel(): String {
        val capabilities = activeCapabilities() ?: return "Offline"
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Wi‑Fi"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Cellular"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> "Bluetooth"
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) -> "Connected"
            else -> "Offline"
        }
    }

    fun localIpv4Address(): String? = runCatching {
        NetworkInterface.getNetworkInterfaces().asSequence()
            .filter { it.isUp && !it.isLoopback }
            .flatMap { it.inetAddresses.asSequence() }
            .filterIsInstance<Inet4Address>()
            .map { it.hostAddress }
            .firstOrNull { !it.startsWith("127.") }
    }.getOrNull()

    fun remoteBaseUrl(port: Int): String? = localIpv4Address()?.let { "http://$it:$port" }

    private fun activeCapabilities(): NetworkCapabilities? {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = manager.activeNetwork ?: return null
        return manager.getNetworkCapabilities(network)
    }
}

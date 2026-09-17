package com.sere.filemanager.wear

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkStatus(private val context: Context) {
    fun isConnected(): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = manager.activeNetwork ?: return false
        val capabilities = manager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun connectionLabel(): String {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = manager.activeNetwork ?: return "Offline"
        val capabilities = manager.getNetworkCapabilities(network) ?: return "Offline"
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Wi‑Fi"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Cellular"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> "Bluetooth"
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) -> "Connected"
            else -> "Offline"
        }
    }
}

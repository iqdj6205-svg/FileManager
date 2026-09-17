package com.sere.filemanager.core.remote

data class RemoteServerHealth(
    val networkAvailable: Boolean,
    val localAddress: String? = null,
    val batteryPercent: Int? = null,
    val warnings: List<String> = emptyList(),
) {
    val canStart: Boolean get() = networkAvailable && warnings.none { it.contains("critical", ignoreCase = true) }
}

class RemoteServerHealthPolicy {
    fun evaluate(networkAvailable: Boolean, localAddress: String?, batteryPercent: Int?): RemoteServerHealth {
        val warnings = buildList {
            if (!networkAvailable) add("No network connection available")
            if (localAddress.isNullOrBlank()) add("No local IP address detected")
            if (batteryPercent != null && batteryPercent <= 15) add("Battery critically low")
            else if (batteryPercent != null && batteryPercent <= 25) add("Battery low; auto-stop recommended")
        }
        return RemoteServerHealth(networkAvailable, localAddress, batteryPercent, warnings)
    }
}

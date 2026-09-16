package com.sere.filemanager.core.remote

import com.sere.filemanager.core.model.RemoteSession

data class RemoteStatus(
    val session: RemoteSession,
    val config: RemoteConfig,
    val activeConnections: Int = 0,
    val bytesTransferred: Long = 0,
)

class RemoteSafetyPolicy {
    fun canStartServer(batteryPercent: Int?, config: RemoteConfig): Boolean {
        if (batteryPercent != null && batteryPercent < 20) return false
        return config.localNetworkOnly
    }

    fun shouldAutoStop(startedAtMillis: Long?, nowMillis: Long, config: RemoteConfig): Boolean {
        if (startedAtMillis == null) return false
        val elapsedMinutes = (nowMillis - startedAtMillis) / 60_000
        return elapsedMinutes >= config.autoStopMinutes
    }
}

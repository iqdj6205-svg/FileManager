package com.sere.filemanager.wear

import com.sere.filemanager.core.remote.RemoteConfig

class RemoteLifecyclePolicy {
    fun shouldAllowStart(batteryPercent: Int?, config: RemoteConfig): Boolean {
        if (batteryPercent != null && batteryPercent < 20) return false
        return config.localNetworkOnly
    }

    fun shouldStopForTimeout(startedAtMillis: Long?, nowMillis: Long, config: RemoteConfig): Boolean {
        if (startedAtMillis == null) return false
        return (nowMillis - startedAtMillis) >= config.autoStopMinutes * 60_000L
    }
}

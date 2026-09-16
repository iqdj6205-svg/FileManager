package com.sere.filemanager.core.media

data class ThumbnailPolicy(
    val enabled: Boolean = true,
    val maxMemoryItems: Int = 100,
    val generateBelowBatteryPercent: Int = 25,
)

class ThumbnailPolicyChecker {
    fun canGenerate(policy: ThumbnailPolicy, batteryPercent: Int?): Boolean {
        if (!policy.enabled) return false
        if (batteryPercent != null && batteryPercent < policy.generateBelowBatteryPercent) return false
        return true
    }
}

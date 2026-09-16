package com.sere.filemanager.core.model

enum class PairingState { NotPaired, Pairing, Paired, Error }

data class WatchDevice(
    val id: String,
    val name: String,
    val batteryPercent: Int? = null,
    val storageFreeBytes: Long? = null,
    val storageTotalBytes: Long? = null,
)

data class CompanionState(
    val pairingState: PairingState = PairingState.NotPaired,
    val pairedDevice: WatchDevice? = null,
    val lastError: String? = null,
)

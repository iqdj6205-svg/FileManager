package com.sere.filemanager.core.model

/**
 * Describes which role a UI surface is currently playing.
 * Wear is always standalone-capable; phone can act as a full manager, companion, or both.
 */
enum class ProductMode {
    StandaloneWatchManager,
    PhoneFileManager,
    PhoneWatchCompanion,
}

data class ProductCapability(
    val name: String,
    val availableOnWatch: Boolean,
    val availableOnPhone: Boolean,
    val requiresCompanion: Boolean = false,
)

object CoreProductCapabilities {
    val fileBrowsing = ProductCapability("File browsing", availableOnWatch = true, availableOnPhone = true)
    val fileOperations = ProductCapability("File operations", availableOnWatch = true, availableOnPhone = true)
    val mediaTools = ProductCapability("Media tools", availableOnWatch = true, availableOnPhone = true)
    val remoteServer = ProductCapability("Remote server", availableOnWatch = true, availableOnPhone = true)
    val watchControlFromPhone = ProductCapability("Watch control from phone", availableOnWatch = false, availableOnPhone = true, requiresCompanion = true)
}

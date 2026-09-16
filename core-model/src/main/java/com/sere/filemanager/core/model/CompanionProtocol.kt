package com.sere.filemanager.core.model

enum class CompanionCommandType { Pair, Ping, StartRemoteServer, StopRemoteServer, SendFile, RequestStorageStatus }

data class CompanionCommand(
    val id: String,
    val type: CompanionCommandType,
    val payload: String? = null,
    val createdAtMillis: Long = System.currentTimeMillis(),
)

data class CompanionCommandResult(
    val commandId: String,
    val success: Boolean,
    val message: String? = null,
)

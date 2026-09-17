package com.sere.filemanager.core.wearbridge

/**
 * Small dependency-free string codec for early bridge messages.
 * Later this can be replaced with kotlinx.serialization once protocol stabilizes.
 */
object WearBridgeCodec {
    fun encode(command: WearBridgeCommand): ByteArray = listOf(
        command.id,
        command.type.name,
        command.createdAtMillis.toString(),
        command.payload.orEmpty(),
    ).joinToString(separator = "\u001f") { it.replace("\u001f", " ") }.toByteArray()

    fun decodeCommand(bytes: ByteArray): WearBridgeCommand? {
        val parts = bytes.toString(Charsets.UTF_8).split("\u001f")
        val id = parts.getOrNull(0) ?: return null
        val type = parts.getOrNull(1)?.let { runCatching { WearBridgeCommandType.valueOf(it) }.getOrNull() } ?: return null
        val created = parts.getOrNull(2)?.toLongOrNull() ?: System.currentTimeMillis()
        val payload = parts.getOrNull(3)?.takeIf { it.isNotBlank() }
        return WearBridgeCommand(id = id, type = type, payload = payload, createdAtMillis = created)
    }

    fun encode(result: WearBridgeCommandResult): ByteArray = listOf(
        result.commandId,
        result.success.toString(),
        result.message.orEmpty(),
        result.payload.orEmpty(),
    ).joinToString(separator = "\u001f") { it.replace("\u001f", " ") }.toByteArray()

    fun decodeResult(bytes: ByteArray): WearBridgeCommandResult? {
        val parts = bytes.toString(Charsets.UTF_8).split("\u001f")
        return WearBridgeCommandResult(
            commandId = parts.getOrNull(0) ?: return null,
            success = parts.getOrNull(1)?.toBooleanStrictOrNull() ?: false,
            message = parts.getOrNull(2)?.takeIf { it.isNotBlank() },
            payload = parts.getOrNull(3)?.takeIf { it.isNotBlank() },
        )
    }
}

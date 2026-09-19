package com.sere.filemanager.core.wearbridge

/**
 * Small dependency-free string codec for early bridge messages.
 * Later this can be replaced with kotlinx.serialization once protocol stabilizes.
 */
object WearBridgeCodec {
    private fun b64Encode(raw: String): String = try { java.util.Base64.getEncoder().encodeToString(raw.toByteArray(Charsets.UTF_8)) } catch (_: Exception) { raw }
    private fun b64Decode(encoded: String?): String? {
        if (encoded.isNullOrBlank()) return null
        return try { String(java.util.Base64.getDecoder().decode(encoded), Charsets.UTF_8).takeIf { it.isNotBlank() } } catch (_: Exception) { encoded.takeIf { it.isNotBlank() } }
    }

    fun encode(command: WearBridgeCommand): ByteArray = listOf(
        command.id,
        command.type.name,
        command.createdAtMillis.toString(),
        b64Encode(command.payload.orEmpty()),
    ).joinToString(separator = "\u001f").toByteArray()

    fun decodeCommand(bytes: ByteArray): WearBridgeCommand? {
        val parts = bytes.toString(Charsets.UTF_8).split("\u001f")
        val id = parts.getOrNull(0) ?: return null
        val type = parts.getOrNull(1)?.let { runCatching { WearBridgeCommandType.valueOf(it) }.getOrNull() } ?: return null
        val created = parts.getOrNull(2)?.toLongOrNull() ?: System.currentTimeMillis()
        val payload = b64Decode(parts.getOrNull(3))
        return WearBridgeCommand(id = id, type = type, payload = payload, createdAtMillis = created)
    }

    fun encode(result: WearBridgeCommandResult): ByteArray = listOf(
        result.commandId,
        result.success.toString(),
        b64Encode(result.message.orEmpty()),
        b64Encode(result.payload.orEmpty()),
    ).joinToString(separator = "\u001f").toByteArray()

    fun decodeResult(bytes: ByteArray): WearBridgeCommandResult? {
        val parts = bytes.toString(Charsets.UTF_8).split("\u001f")
        return WearBridgeCommandResult(
            commandId = parts.getOrNull(0) ?: return null,
            success = parts.getOrNull(1)?.toBooleanStrictOrNull() ?: false,
            message = b64Decode(parts.getOrNull(2)),
            payload = b64Decode(parts.getOrNull(3)),
        )
    }
}

package com.sere.filemanager.core.wearbridge

object WearBridgeTransferCodec {
    fun encode(request: WearFileTransferRequest): String = listOf(
        request.id,
        request.fileName,
        request.sourcePath.orEmpty(),
        request.targetPath,
        request.direction.name,
        request.sizeBytes?.toString().orEmpty(),
    ).joinToString("\u001f") { it.replace("\u001f", " ") }

    fun decodeRequest(value: String?): WearFileTransferRequest? {
        if (value.isNullOrBlank()) return null
        val p = value.split("\u001f")
        return WearFileTransferRequest(
            id = p.getOrNull(0) ?: return null,
            fileName = p.getOrNull(1) ?: return null,
            sourcePath = p.getOrNull(2)?.takeIf { it.isNotBlank() },
            targetPath = p.getOrNull(3) ?: return null,
            direction = p.getOrNull(4)?.let { runCatching { WearTransferDirection.valueOf(it) }.getOrNull() } ?: return null,
            sizeBytes = p.getOrNull(5)?.toLongOrNull(),
        )
    }
}

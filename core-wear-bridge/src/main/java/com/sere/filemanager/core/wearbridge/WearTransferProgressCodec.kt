package com.sere.filemanager.core.wearbridge

import com.google.android.gms.wearable.DataMap

object WearTransferProgressCodec {
    private const val ID = "id"
    private const val STATE = "state"
    private const val TRANSFERRED = "transferred"
    private const val TOTAL = "total"
    private const val MESSAGE = "message"

    fun toDataMap(progress: WearFileTransferProgress): DataMap = DataMap().apply {
        putString(ID, progress.requestId)
        putString(STATE, progress.state.name)
        putLong(TRANSFERRED, progress.transferredBytes)
        progress.totalBytes?.let { putLong(TOTAL, it) }
        putString(MESSAGE, progress.message.orEmpty())
    }

    fun fromDataMap(map: DataMap): WearFileTransferProgress? {
        val id = map.getString(ID)?.takeIf { it.isNotBlank() } ?: return null
        val state = map.getString(STATE)?.let { runCatching { WearTransferState.valueOf(it) }.getOrNull() } ?: return null
        return WearFileTransferProgress(
            requestId = id,
            state = state,
            transferredBytes = map.getLong(TRANSFERRED, 0L),
            totalBytes = if (map.containsKey(TOTAL)) map.getLong(TOTAL) else null,
            message = map.getString(MESSAGE)?.takeIf { it.isNotBlank() },
        )
    }
}

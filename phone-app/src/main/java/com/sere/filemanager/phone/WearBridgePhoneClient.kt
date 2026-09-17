package com.sere.filemanager.phone

import android.content.Context
import com.google.android.gms.wearable.Wearable
import com.sere.filemanager.core.wearbridge.WearBridgeCodec
import com.sere.filemanager.core.wearbridge.WearBridgeCommand
import com.sere.filemanager.core.wearbridge.WearBridgeCommandResult
import com.sere.filemanager.core.wearbridge.WearBridgeCommandType
import com.sere.filemanager.core.wearbridge.WearBridgePaths
import kotlinx.coroutines.tasks.await

class WearBridgePhoneClient(private val context: Context) {
    suspend fun sendToFirstWatch(type: WearBridgeCommandType, payload: String? = null): Result<String> = runCatching {
        val nodes = Wearable.getNodeClient(context).connectedNodes.await()
        val node = nodes.firstOrNull() ?: error("No connected Wear OS watch")
        val command = WearBridgeCommand(id = "wear-${System.currentTimeMillis()}", type = type, payload = payload)
        Wearable.getMessageClient(context).sendMessage(node.id, WearBridgePaths.MESSAGE_COMMAND, WearBridgeCodec.encode(command)).await()
        "Sent ${type.name} to ${node.displayName}"
    }

    suspend fun prepareFileTransfer(payload: String): Result<String> = sendToFirstWatch(WearBridgeCommandType.PrepareFileTransfer, payload)

    suspend fun connectedWatchNames(): Result<List<String>> = runCatching { Wearable.getNodeClient(context).connectedNodes.await().map { it.displayName } }

    fun latestResult(): WearBridgeCommandResult? = PhoneBridgeStatusStore.latest()

    fun latestResultText(): String? {
        val result = latestResult() ?: return null
        return buildString { append(if (result.success) "OK" else "Failed"); append(": "); append(result.message ?: result.commandId); result.payload?.let { append(" — ").append(it) } }
    }
}

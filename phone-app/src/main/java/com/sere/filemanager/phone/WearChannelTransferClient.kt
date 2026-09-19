package com.sere.filemanager.phone

import android.content.Context
import android.net.Uri
import com.google.android.gms.wearable.Wearable
import com.sere.filemanager.core.wearbridge.WearBridgePaths
import com.sere.filemanager.core.wearbridge.WearFileTransferProgress
import com.sere.filemanager.core.wearbridge.WearFileTransferRequest
import com.sere.filemanager.core.wearbridge.WearTransferState
import kotlinx.coroutines.tasks.await

class WearChannelTransferClient(private val context: Context) {
    suspend fun sendFileToFirstWatch(
        request: WearFileTransferRequest,
        uri: Uri,
        onProgress: (WearFileTransferProgress) -> Unit = {},
    ): Result<WearFileTransferProgress> = runCatching {
        val node = Wearable.getNodeClient(context).connectedNodes.await().firstOrNull() ?: error("No connected Wear OS watch")
        val channelClient = Wearable.getChannelClient(context)
        val channel = channelClient.openChannel(node.id, WearBridgePaths.CHANNEL_FILE_TRANSFER).await()
        val output = channelClient.getOutputStream(channel).await()
        var transferred = 0L
        onProgress(WearFileTransferProgress(request.id, WearTransferState.Transferring, transferredBytes = 0L, totalBytes = request.sizeBytes, message = "Sending to ${node.displayName}"))
        try {
            context.contentResolver.openInputStream(uri).use { input ->
                requireNotNull(input) { "Cannot open selected file" }
                output.use { out ->
                    val buffer = ByteArray(32 * 1024)
                    while (true) {
                        val read = input.read(buffer)
                        if (read < 0) break
                        out.write(buffer, 0, read)
                        transferred += read
                        onProgress(WearFileTransferProgress(request.id, WearTransferState.Transferring, transferredBytes = transferred, totalBytes = request.sizeBytes, message = "Sending ${request.fileName}"))
                    }
                }
            }
        } finally {
            runCatching { channelClient.close(channel).await() }
        }
        WearFileTransferProgress(request.id, WearTransferState.Completed, transferredBytes = transferred, totalBytes = request.sizeBytes, message = "Sent to ${node.displayName}")
    }
}

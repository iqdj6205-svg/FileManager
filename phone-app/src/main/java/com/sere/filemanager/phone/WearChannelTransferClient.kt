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
    suspend fun sendFileToFirstWatch(request: WearFileTransferRequest, uri: Uri): Result<WearFileTransferProgress> = runCatching {
        val node = Wearable.getNodeClient(context).connectedNodes.await().firstOrNull() ?: error("No connected Wear OS watch")
        val channel = Wearable.getChannelClient(context).openChannel(node.id, WearBridgePaths.CHANNEL_FILE_TRANSFER).await()
        val output = Wearable.getChannelClient(context).getOutputStream(channel).await()
        context.contentResolver.openInputStream(uri).use { input ->
            requireNotNull(input) { "Cannot open selected file" }
            output.use { out -> input.copyTo(out, bufferSize = 32 * 1024) }
        }
        WearFileTransferProgress(request.id, WearTransferState.Completed, totalBytes = request.sizeBytes, message = "Sent to ${node.displayName}")
    }
}

package com.sere.filemanager.wear

import com.google.android.gms.wearable.ChannelClient
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import com.sere.filemanager.core.wearbridge.WearBridgePaths
import com.sere.filemanager.core.wearbridge.WearFileTransferProgress
import com.sere.filemanager.core.wearbridge.WearTransferState
import java.io.File

class WearChannelListenerService : WearableListenerService() {
    override fun onChannelOpened(channel: ChannelClient.Channel) {
        if (channel.path != WearBridgePaths.CHANNEL_FILE_TRANSFER) return
        val request = WearPendingTransferStore.consume()
        val transferId = request?.id ?: "transfer-${System.currentTimeMillis()}"
        val publisher = WearTransferProgressPublisher(this)
        val target = resolveTargetFile(request?.targetPath, request?.fileName)
        target.parentFile?.mkdirs()
        publisher.publish(WearFileTransferProgress(transferId, WearTransferState.OpeningChannel, totalBytes = request?.sizeBytes, message = "Receiving ${request?.fileName ?: target.name}"))
        Wearable.getChannelClient(this).getInputStream(channel).addOnSuccessListener { input ->
            runCatching {
                var copied = 0L
                input.use { source ->
                    target.outputStream().use { out ->
                        val buffer = ByteArray(32 * 1024)
                        while (true) {
                            val read = source.read(buffer)
                            if (read <= 0) break
                            out.write(buffer, 0, read)
                            copied += read
                        }
                    }
                }
                publisher.publish(WearFileTransferProgress(transferId, WearTransferState.Completed, copied, request?.sizeBytes, "Received ${target.name}"))
            }.onFailure { error ->
                publisher.publish(WearFileTransferProgress(transferId, WearTransferState.Failed, totalBytes = request?.sizeBytes, message = error.message ?: "Receive failed"))
            }
        }.addOnFailureListener { error ->
            publisher.publish(WearFileTransferProgress(transferId, WearTransferState.Failed, totalBytes = request?.sizeBytes, message = error.message ?: "Channel failed"))
        }
    }

    private fun resolveTargetFile(targetPath: String?, fileName: String?): File {
        val rawName = fileName ?: "received-${System.currentTimeMillis()}.bin"
        val safeName = com.sere.filemanager.core.files.OperationNamePolicy.sanitizeInputName(rawName).takeIf { com.sere.filemanager.core.files.OperationNamePolicy.isValidFileName(it) } ?: "received-${System.currentTimeMillis()}.bin"
        val requested = targetPath?.takeIf { it.isNotBlank() && com.sere.filemanager.core.files.PathSafety.explainIfBlocked(it) == null }
        val targetFile = requested?.let { File(it) }
        val safeTarget = targetFile?.takeIf { file ->
            file.absolutePath.startsWith(filesDir.absolutePath) || file.absolutePath.startsWith("/sdcard/")
        }
        return safeTarget ?: File(File(filesDir, "received"), safeName)
    }
}

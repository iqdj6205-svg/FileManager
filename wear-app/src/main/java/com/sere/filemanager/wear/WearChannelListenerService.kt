package com.sere.filemanager.wear

import com.google.android.gms.wearable.ChannelClient
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import com.sere.filemanager.core.wearbridge.WearBridgePaths
import java.io.File

class WearChannelListenerService : WearableListenerService() {
    override fun onChannelOpened(channel: ChannelClient.Channel) {
        if (channel.path != WearBridgePaths.CHANNEL_FILE_TRANSFER) return
        val request = WearPendingTransferStore.consume()
        val target = resolveTargetFile(request?.targetPath, request?.fileName)
        target.parentFile?.mkdirs()
        Wearable.getChannelClient(this).getInputStream(channel).addOnSuccessListener { input ->
            runCatching { input.use { source -> target.outputStream().use { out -> source.copyTo(out, bufferSize = 32 * 1024) } } }
        }
    }

    private fun resolveTargetFile(targetPath: String?, fileName: String?): File {
        val safeName = (fileName ?: "received-${System.currentTimeMillis()}.bin").replace('/', '_').replace('\\', '_')
        val requested = targetPath?.takeIf { it.isNotBlank() }
        return if (requested != null && requested.startsWith(filesDir.absolutePath)) File(requested) else File(File(filesDir, "received"), safeName)
    }
}

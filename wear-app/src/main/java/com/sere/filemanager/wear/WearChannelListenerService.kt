package com.sere.filemanager.wear

import com.google.android.gms.wearable.ChannelClient
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import com.sere.filemanager.core.wearbridge.WearBridgePaths
import java.io.File

class WearChannelListenerService : WearableListenerService() {
    override fun onChannelOpened(channel: ChannelClient.Channel) {
        if (channel.path != WearBridgePaths.CHANNEL_FILE_TRANSFER) return
        val targetDir = File(filesDir, "received")
        targetDir.mkdirs()
        val target = File(targetDir, "received-${System.currentTimeMillis()}.bin")
        val inputTask = Wearable.getChannelClient(this).getInputStream(channel)
        inputTask.addOnSuccessListener { input ->
            runCatching {
                input.use { source -> target.outputStream().use { out -> source.copyTo(out, bufferSize = 32 * 1024) } }
            }
        }
    }
}

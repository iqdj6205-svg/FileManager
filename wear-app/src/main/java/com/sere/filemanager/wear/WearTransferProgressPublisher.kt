package com.sere.filemanager.wear

import android.content.Context
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.sere.filemanager.core.wearbridge.WearBridgePaths
import com.sere.filemanager.core.wearbridge.WearFileTransferProgress
import com.sere.filemanager.core.wearbridge.WearTransferProgressCodec

class WearTransferProgressPublisher(private val context: Context) {
    fun publish(progress: WearFileTransferProgress) {
        val request = PutDataMapRequest.create(WearBridgePaths.DATA_TRANSFER_PROGRESS).apply {
            dataMap.putAll(WearTransferProgressCodec.toDataMap(progress))
            dataMap.putLong("updated_at", System.currentTimeMillis())
        }.asPutDataRequest().setUrgent()
        Wearable.getDataClient(context).putDataItem(request)
    }
}

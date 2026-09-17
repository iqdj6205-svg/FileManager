package com.sere.filemanager.phone

import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import com.sere.filemanager.core.wearbridge.WearBridgeCodec
import com.sere.filemanager.core.wearbridge.WearBridgePaths
import com.sere.filemanager.core.wearbridge.WearBridgeStatusCodec
import com.sere.filemanager.core.wearbridge.WearTransferProgressCodec

class PhoneBridgeListenerService : WearableListenerService() {
    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path != WearBridgePaths.MESSAGE_COMMAND_RESULT) return
        val result = WearBridgeCodec.decodeResult(messageEvent.data) ?: return
        PhoneBridgeStatusStore.update(result)
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            if (event.type != DataEvent.TYPE_CHANGED) return@forEach
            val map = DataMapItem.fromDataItem(event.dataItem).dataMap
            when (event.dataItem.uri.path) {
                WearBridgePaths.DATA_REMOTE_STATUS -> PhoneRemoteStatusStore.update(WearBridgeStatusCodec.remoteStatusFromDataMap(map))
                WearBridgePaths.DATA_TRANSFER_PROGRESS -> WearTransferProgressCodec.fromDataMap(map)?.let { PhoneTransferProgressStore.update(it) }
            }
        }
    }
}

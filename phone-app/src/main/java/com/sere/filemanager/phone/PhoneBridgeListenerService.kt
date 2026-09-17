package com.sere.filemanager.phone

import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import com.sere.filemanager.core.wearbridge.WearBridgeCodec
import com.sere.filemanager.core.wearbridge.WearBridgePaths

class PhoneBridgeListenerService : WearableListenerService() {
    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path != WearBridgePaths.MESSAGE_COMMAND_RESULT) return
        val result = WearBridgeCodec.decodeResult(messageEvent.data) ?: return
        PhoneBridgeStatusStore.update(result)
    }
}

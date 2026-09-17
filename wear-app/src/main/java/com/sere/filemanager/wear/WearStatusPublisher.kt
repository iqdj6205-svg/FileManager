package com.sere.filemanager.wear

import android.content.Context
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.sere.filemanager.core.model.RemoteServerState
import com.sere.filemanager.core.remote.RemoteServerStatusStore
import com.sere.filemanager.core.wearbridge.WearBridgePaths
import com.sere.filemanager.core.wearbridge.WearBridgeRemoteStatus
import com.sere.filemanager.core.wearbridge.WearBridgeStatusCodec

class WearStatusPublisher(private val context: Context) {
    fun publishRemoteStatus() {
        val session = RemoteServerStatusStore.current()
        val status = WearBridgeRemoteStatus(
            running = session.state == RemoteServerState.Running,
            url = session.url,
            pin = session.pin,
            networkLabel = NetworkStatus(context).connectionLabel(),
            batteryPercent = BatteryMonitor(context).batteryPercent(),
        )
        val request = PutDataMapRequest.create(WearBridgePaths.DATA_REMOTE_STATUS).apply {
            dataMap.putAll(WearBridgeStatusCodec.remoteStatusToDataMap(status))
            dataMap.putLong("updated_at", System.currentTimeMillis())
        }.asPutDataRequest().setUrgent()
        Wearable.getDataClient(context).putDataItem(request)
    }
}

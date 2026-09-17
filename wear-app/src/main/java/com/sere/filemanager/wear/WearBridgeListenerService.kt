package com.sere.filemanager.wear

import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import com.sere.filemanager.core.wearbridge.WearBridgeCodec
import com.sere.filemanager.core.wearbridge.WearBridgeCommand
import com.sere.filemanager.core.wearbridge.WearBridgeCommandResult
import com.sere.filemanager.core.wearbridge.WearBridgeCommandType
import com.sere.filemanager.core.wearbridge.WearBridgePaths
import com.sere.filemanager.core.wearbridge.WearBridgeSettingsStringCodec
import com.sere.filemanager.core.wearbridge.WearBridgeTransferCodec

class WearBridgeListenerService : WearableListenerService() {
    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path != WearBridgePaths.MESSAGE_COMMAND) return
        val command = WearBridgeCodec.decodeCommand(messageEvent.data) ?: return
        val result = handle(command)
        Wearable.getMessageClient(this).sendMessage(messageEvent.sourceNodeId, WearBridgePaths.MESSAGE_COMMAND_RESULT, WearBridgeCodec.encode(result))
    }

    private fun handle(command: WearBridgeCommand): WearBridgeCommandResult = when (command.type) {
        WearBridgeCommandType.StartWatchServer -> { RemoteServiceController(this).start(); WearBridgeCommandResult(command.id, true, "Watch server start requested") }
        WearBridgeCommandType.StopWatchServer -> { RemoteServiceController(this).stop(); WearBridgeCommandResult(command.id, true, "Watch server stop requested") }
        WearBridgeCommandType.GetWatchServerStatus -> { val session = com.sere.filemanager.core.remote.RemoteServerStatusStore.current(); WearBridgeCommandResult(command.id, true, "Watch server status", "${session.state}|${session.url.orEmpty()}|${session.pin.orEmpty()}") }
        WearBridgeCommandType.SyncSettings -> {
            val settings = WearBridgeSettingsStringCodec.decode(command.payload)
            if (settings == null) WearBridgeCommandResult(command.id, false, "Invalid settings payload") else { WearSettingsStore.update(settings); WearBridgeCommandResult(command.id, true, "Settings synced") }
        }
        WearBridgeCommandType.GetStorageStatus -> WearBridgeCommandResult(command.id, true, "Storage status reserved")
        WearBridgeCommandType.PrepareFileTransfer -> {
            val request = WearBridgeTransferCodec.decodeRequest(command.payload)
            if (request == null) WearBridgeCommandResult(command.id, false, "Invalid transfer metadata") else { WearPendingTransferStore.update(request); WearBridgeCommandResult(command.id, true, "Ready to receive ${request.fileName}") }
        }
        WearBridgeCommandType.SendFileToWatch -> WearBridgeCommandResult(command.id, false, "Use channel transfer")
        WearBridgeCommandType.RequestFileFromWatch -> WearBridgeCommandResult(command.id, false, "Use channel transfer")
    }
}

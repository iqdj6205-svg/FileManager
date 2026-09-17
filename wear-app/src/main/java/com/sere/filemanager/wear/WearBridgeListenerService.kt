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

class WearBridgeListenerService : WearableListenerService() {
    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path != WearBridgePaths.MESSAGE_COMMAND) return
        val command = WearBridgeCodec.decodeCommand(messageEvent.data) ?: return
        val result = handle(command)
        Wearable.getMessageClient(this).sendMessage(messageEvent.sourceNodeId, WearBridgePaths.MESSAGE_COMMAND_RESULT, WearBridgeCodec.encode(result))
    }

    private fun handle(command: WearBridgeCommand): WearBridgeCommandResult = when (command.type) {
        WearBridgeCommandType.StartWatchServer -> {
            RemoteServiceController(this).start()
            WearBridgeCommandResult(command.id, success = true, message = "Watch server start requested")
        }
        WearBridgeCommandType.StopWatchServer -> {
            RemoteServiceController(this).stop()
            WearBridgeCommandResult(command.id, success = true, message = "Watch server stop requested")
        }
        WearBridgeCommandType.GetWatchServerStatus -> {
            val session = com.sere.filemanager.core.remote.RemoteServerStatusStore.current()
            WearBridgeCommandResult(command.id, success = true, message = "Watch server status", payload = "${session.state}|${session.url.orEmpty()}|${session.pin.orEmpty()}")
        }
        WearBridgeCommandType.SyncSettings -> {
            val settings = WearBridgeSettingsStringCodec.decode(command.payload)
            if (settings == null) WearBridgeCommandResult(command.id, success = false, message = "Invalid settings payload")
            else {
                WearSettingsStore.update(settings)
                WearBridgeCommandResult(command.id, success = true, message = "Settings synced")
            }
        }
        WearBridgeCommandType.GetStorageStatus -> WearBridgeCommandResult(command.id, success = true, message = "Storage status reserved")
        WearBridgeCommandType.SendFileToWatch -> WearBridgeCommandResult(command.id, success = false, message = "Use channel transfer")
        WearBridgeCommandType.RequestFileFromWatch -> WearBridgeCommandResult(command.id, success = false, message = "Use channel transfer")
    }
}

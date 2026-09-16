package com.sere.filemanager.phone

import com.sere.filemanager.core.model.CompanionCommand
import com.sere.filemanager.core.model.CompanionCommandResult
import com.sere.filemanager.core.model.CompanionCommandType

class CompanionProtocolClient {
    fun ping(): CompanionCommand = CompanionCommand(id = newId(), type = CompanionCommandType.Ping)
    fun startRemoteServer(): CompanionCommand = CompanionCommand(id = newId(), type = CompanionCommandType.StartRemoteServer)
    fun stopRemoteServer(): CompanionCommand = CompanionCommand(id = newId(), type = CompanionCommandType.StopRemoteServer)

    fun handleResult(command: CompanionCommand, success: Boolean, message: String?): CompanionCommandResult =
        CompanionCommandResult(commandId = command.id, success = success, message = message)

    private fun newId(): String = "cmd-${System.currentTimeMillis()}"
}

package com.sere.filemanager.phone

import com.sere.filemanager.core.model.FileTransfer

class TransferQueue {
    private val transfers = mutableListOf<FileTransfer>()

    fun list(): List<FileTransfer> = transfers.toList()

    fun enqueue(transfer: FileTransfer): List<FileTransfer> {
        transfers.add(transfer)
        return list()
    }

    fun clearCompleted(): List<FileTransfer> {
        transfers.removeAll { it.state.name == "Completed" }
        return list()
    }
}

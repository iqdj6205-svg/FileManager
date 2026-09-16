package com.sere.filemanager.core.remote

data class RemoteRenameRequest(val path: String, val newName: String, val pin: String?)
data class RemoteDeleteRequest(val path: String, val pin: String?)
data class RemoteMkdirRequest(val parentPath: String, val name: String, val pin: String?)
data class RemoteUploadRequest(val targetPath: String, val fileName: String, val pin: String?)

package com.sere.filemanager.core.model

enum class RemoteServerState { Stopped, Starting, Running, Stopping, Error }

data class RemoteSession(
    val state: RemoteServerState = RemoteServerState.Stopped,
    val url: String? = null,
    val pin: String? = null,
    val startedAtMillis: Long? = null,
    val errorMessage: String? = null,
)

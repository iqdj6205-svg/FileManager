package com.sere.filemanager.wear

import com.sere.filemanager.core.model.RemoteServerState
import com.sere.filemanager.core.model.RemoteSession

data class RemoteUiState(
    val running: Boolean,
    val title: String,
    val subtitle: String,
    val actionLabel: String,
)

object RemoteUiMapper {
    fun map(session: RemoteSession): RemoteUiState {
        val running = session.state == RemoteServerState.Running
        return RemoteUiState(
            running = running,
            title = if (running) "Server running" else "Server stopped",
            subtitle = if (running) "${session.url.orEmpty()} PIN ${session.pin.orEmpty()}" else "Start only on trusted Wi-Fi",
            actionLabel = if (running) "Stop server" else "Start HTTP",
        )
    }
}

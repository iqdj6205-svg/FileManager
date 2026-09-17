package com.sere.filemanager.core.remote

import com.sere.filemanager.core.model.RemoteServerState
import com.sere.filemanager.core.model.RemoteSession

object RemoteServerStatusStore {
    @Volatile private var session: RemoteSession = RemoteSession(state = RemoteServerState.Stopped)

    fun update(newSession: RemoteSession) { session = newSession }
    fun current(): RemoteSession = session
    fun clear() { session = RemoteSession(state = RemoteServerState.Stopped) }
}

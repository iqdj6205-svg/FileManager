package com.sere.filemanager.wear

import android.content.Context
import com.sere.filemanager.core.model.RemoteServerState
import com.sere.filemanager.core.model.RemoteSession
import com.sere.filemanager.core.remote.RemoteServerController
import com.sere.filemanager.core.remote.RemoteServerStatusStore

class ServiceBackedRemoteController(
    context: Context,
) : RemoteServerController {
    private val serviceController = RemoteServiceController(context.applicationContext)

    override suspend fun start(): RemoteSession {
        val requested = serviceController.start()
        return if (requested) RemoteServerStatusStore.current().takeIf { it.state == RemoteServerState.Running }
            ?: RemoteSession(state = RemoteServerState.Starting, startedAtMillis = System.currentTimeMillis())
        else RemoteSession(state = RemoteServerState.Stopped)
    }

    override suspend fun stop(): RemoteSession {
        serviceController.stop()
        return RemoteServerStatusStore.current().takeIf { it.state != RemoteServerState.Running }
            ?: RemoteSession(state = RemoteServerState.Stopped)
    }

    override fun currentSession(): RemoteSession = RemoteServerStatusStore.current()
}

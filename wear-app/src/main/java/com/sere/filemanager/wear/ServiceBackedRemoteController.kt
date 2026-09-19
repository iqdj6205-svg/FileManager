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
        if (!requested) return RemoteSession(state = RemoteServerState.Stopped)
        repeat(10) {
            kotlinx.coroutines.delay(200)
            val current = RemoteServerStatusStore.current()
            if (current.state == RemoteServerState.Running) return current
        }
        return RemoteServerStatusStore.current().takeIf { it.state == RemoteServerState.Running }
            ?: RemoteSession(state = RemoteServerState.Starting, startedAtMillis = System.currentTimeMillis())
    }

    override suspend fun stop(): RemoteSession {
        serviceController.stop()
        return RemoteServerStatusStore.current().takeIf { it.state != RemoteServerState.Running }
            ?: RemoteSession(state = RemoteServerState.Stopped)
    }

    override fun currentSession(): RemoteSession = RemoteServerStatusStore.current()
}

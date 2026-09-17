package com.sere.filemanager.wear

import android.content.Context
import com.sere.filemanager.core.model.RemoteServerState
import com.sere.filemanager.core.model.RemoteSession
import com.sere.filemanager.core.remote.RemoteServerController

class ServiceBackedRemoteController(
    context: Context,
) : RemoteServerController {
    private val serviceController = RemoteServiceController(context.applicationContext)
    private var session = RemoteSession(state = RemoteServerState.Stopped)

    override suspend fun start(): RemoteSession {
        serviceController.start()
        session = RemoteSession(
            state = RemoteServerState.Running,
            url = "Shown in notification",
            pin = "Shown in notification",
            startedAtMillis = System.currentTimeMillis(),
        )
        return session
    }

    override suspend fun stop(): RemoteSession {
        serviceController.stop()
        session = RemoteSession(state = RemoteServerState.Stopped)
        return session
    }

    override fun currentSession(): RemoteSession = session
}

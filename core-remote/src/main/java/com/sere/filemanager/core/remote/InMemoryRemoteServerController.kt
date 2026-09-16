package com.sere.filemanager.core.remote

import com.sere.filemanager.core.model.RemoteServerState
import com.sere.filemanager.core.model.RemoteSession
import kotlin.random.Random

class InMemoryRemoteServerController : RemoteServerController {
    private var session = RemoteSession()

    override suspend fun start(): RemoteSession {
        val pin = Random.nextInt(100000, 999999).toString()
        session = RemoteSession(
            state = RemoteServerState.Running,
            url = "http://watch.local:8080",
            pin = pin,
            startedAtMillis = System.currentTimeMillis(),
        )
        return session
    }

    override suspend fun stop(): RemoteSession {
        session = RemoteSession(state = RemoteServerState.Stopped)
        return session
    }

    override fun currentSession(): RemoteSession = session
}

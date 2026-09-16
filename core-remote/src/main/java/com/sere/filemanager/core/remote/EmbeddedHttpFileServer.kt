package com.sere.filemanager.core.remote

import com.sere.filemanager.core.files.FileRepository
import com.sere.filemanager.core.model.RemoteServerState
import com.sere.filemanager.core.model.RemoteSession
import kotlin.random.Random

class EmbeddedHttpFileServer(
    fileRepository: FileRepository,
    private val config: RemoteConfig = RemoteConfig(),
) : RemoteServerController {
    private val engine = SimpleHttpEngine(fileRepository, config)
    private var session = RemoteSession()

    override suspend fun start(): RemoteSession {
        val pin = Random.nextInt(100000, 999999).toString()
        engine.start(pin)
        session = RemoteSession(
            state = RemoteServerState.Running,
            url = "http://127.0.0.1:${config.port}",
            pin = pin,
            startedAtMillis = System.currentTimeMillis(),
        )
        return session
    }

    override suspend fun stop(): RemoteSession {
        engine.stop()
        session = RemoteSession(state = RemoteServerState.Stopped)
        return session
    }

    override fun currentSession(): RemoteSession = session
}

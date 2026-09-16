package com.sere.filemanager.core.remote

import com.sere.filemanager.core.files.FileRepository
import com.sere.filemanager.core.model.RemoteSession

class EmbeddedHttpFileServer(
    fileRepository: FileRepository,
    private val config: RemoteConfig = RemoteConfig(),
) : RemoteServerController {
    private val engine = SimpleHttpEngine(fileRepository, config)
    private val sessions = RemoteSessionManager()

    override suspend fun start(): RemoteSession {
        val url = "http://127.0.0.1:${config.port}"
        val session = sessions.start(url)
        engine.start(session.pin.orEmpty())
        return session
    }

    override suspend fun stop(): RemoteSession {
        engine.stop()
        return sessions.stop()
    }

    override fun currentSession(): RemoteSession = sessions.current()
}

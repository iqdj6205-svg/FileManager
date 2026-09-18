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
        RemoteServerStatusStore.update(session)
        runCatching { engine.start(session.pin.orEmpty()) }
            .onFailure {
                RemoteServerStatusStore.clear()
                sessions.stop()
                throw it
            }
        return session
    }

    override suspend fun stop(): RemoteSession {
        engine.stop()
        val session = sessions.stop()
        RemoteServerStatusStore.update(session)
        return session
    }

    override fun currentSession(): RemoteSession = RemoteServerStatusStore.current()
}

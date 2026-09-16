package com.sere.filemanager.core.remote

import com.sere.filemanager.core.files.FileRepository
import com.sere.filemanager.core.model.RemoteServerState
import com.sere.filemanager.core.model.RemoteSession
import kotlin.random.Random

/**
 * Lightweight server facade. The real socket implementation will be added behind this API.
 * Keeping it isolated lets the Wear UI, security model, and battery policies compile independently.
 */
class HttpFileServer(
    private val fileRepository: FileRepository,
    private val config: RemoteConfig = RemoteConfig(),
) : RemoteServerController {
    private var session = RemoteSession()

    override suspend fun start(): RemoteSession {
        val pin = Random.nextInt(100000, 999999).toString()
        session = RemoteSession(
            state = RemoteServerState.Running,
            url = "http://127.0.0.1:${config.port}",
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

    suspend fun listDirectory(path: String) = fileRepository.list(path)
}

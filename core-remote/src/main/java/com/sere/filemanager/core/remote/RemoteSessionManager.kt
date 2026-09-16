package com.sere.filemanager.core.remote

import com.sere.filemanager.core.model.RemoteServerState
import com.sere.filemanager.core.model.RemoteSession
import kotlin.random.Random

class RemoteSessionManager(
    private val auditLog: RemoteAuditLog = InMemoryRemoteAuditLog(),
) {
    private var session = RemoteSession()

    fun start(url: String): RemoteSession {
        val pin = Random.nextInt(100000, 999999).toString()
        session = RemoteSession(
            state = RemoteServerState.Running,
            url = url,
            pin = pin,
            startedAtMillis = System.currentTimeMillis(),
        )
        auditLog.record(RemoteAuditEvent(RemoteAuditEventType.ServerStarted, "Remote server started"))
        return session
    }

    fun stop(): RemoteSession {
        session = RemoteSession(state = RemoteServerState.Stopped)
        auditLog.record(RemoteAuditEvent(RemoteAuditEventType.ServerStopped, "Remote server stopped"))
        return session
    }

    fun current(): RemoteSession = session
    fun auditEvents(): List<RemoteAuditEvent> = auditLog.list()
}

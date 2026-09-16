package com.sere.filemanager.core.remote

import com.sere.filemanager.core.model.RemoteSession

interface RemoteServerController {
    suspend fun start(): RemoteSession
    suspend fun stop(): RemoteSession
    fun currentSession(): RemoteSession
}

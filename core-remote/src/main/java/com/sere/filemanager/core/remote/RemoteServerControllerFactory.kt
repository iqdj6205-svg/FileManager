package com.sere.filemanager.core.remote

import com.sere.filemanager.core.files.FileRepository

object RemoteServerControllerFactory {
    fun create(fileRepository: FileRepository, useEmbeddedPrototype: Boolean = true): RemoteServerController {
        return if (useEmbeddedPrototype) EmbeddedHttpFileServer(fileRepository) else InMemoryRemoteServerController()
    }
}

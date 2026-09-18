package com.sere.filemanager.core.remote

import com.sere.filemanager.core.files.FileRepository

object RemoteServerControllerFactory {
    fun create(
        fileRepository: FileRepository,
        useEmbeddedPrototype: Boolean = true,
        config: RemoteConfig = RemoteConfig(),
    ): RemoteServerController {
        return if (useEmbeddedPrototype) EmbeddedHttpFileServer(fileRepository, config) else InMemoryRemoteServerController()
    }
}

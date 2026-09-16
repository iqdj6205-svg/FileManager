package com.sere.filemanager.core.media

class InMemoryMediaRepository : MediaRepository {
    override suspend fun listImages(): List<MediaItem> = emptyList()
    override suspend fun listAudio(): List<MediaItem> = emptyList()
    override suspend fun listVideo(): List<MediaItem> = emptyList()
}

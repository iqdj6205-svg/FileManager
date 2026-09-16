package com.sere.filemanager.core.media

interface MediaRepository {
    suspend fun listImages(): List<MediaItem>
    suspend fun listAudio(): List<MediaItem>
    suspend fun listVideo(): List<MediaItem>
}

package com.sere.filemanager.core.media

class MediaLibraryUseCase(private val mediaRepository: MediaRepository) {
    suspend fun loadLibrary(query: MediaStoreQuery = MediaStoreQuery()): MediaLibraryState {
        val images = if (query.includeImages) mediaRepository.listImages() else emptyList()
        val audio = if (query.includeAudio) mediaRepository.listAudio() else emptyList()
        val video = if (query.includeVideo) mediaRepository.listVideo() else emptyList()
        return MediaLibraryState(
            images = images.take(query.limit),
            audio = audio.take(query.limit),
            video = video.take(query.limit),
        )
    }
}
package com.sere.filemanager.wear

import com.sere.filemanager.core.media.MediaLibraryUseCase
import com.sere.filemanager.core.media.MediaRepository

class WearMediaController(private val library: MediaLibraryUseCase) {
    suspend fun load() = library.loadLibrary()

    companion object {
        fun create(repository: MediaRepository): WearMediaController = WearMediaController(MediaLibraryUseCase(repository))
    }
}

package com.sere.filemanager.phone

import com.sere.filemanager.core.media.MediaLibraryUseCase
import com.sere.filemanager.core.media.MediaRepository

class PhoneMediaController(
    private val mediaLibrary: MediaLibraryUseCase,
) {
    suspend fun loadLibrary() = mediaLibrary.loadLibrary()

    companion object {
        fun create(repository: MediaRepository): PhoneMediaController = PhoneMediaController(MediaLibraryUseCase(repository))
    }
}

package com.sere.filemanager.core.media

import com.sere.filemanager.core.files.FileRepository
import com.sere.filemanager.core.model.FileItem

class MediaLibraryUseCase(private val fileRepository: FileRepository) {
    suspend fun imagesIn(path: String): List<FileItem> = fileRepository.list(path).filter(MediaClassifier::isImage)
    suspend fun audioIn(path: String): List<FileItem> = fileRepository.list(path).filter(MediaClassifier::isAudio)
    suspend fun videoIn(path: String): List<FileItem> = fileRepository.list(path).filter(MediaClassifier::isVideo)
}

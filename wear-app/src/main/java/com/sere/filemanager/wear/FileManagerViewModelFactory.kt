package com.sere.filemanager.wear

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sere.filemanager.core.files.LocalFileRepository
import com.sere.filemanager.core.files.SafeFileOperations
import com.sere.filemanager.core.media.AndroidMediaStoreRepository

class FileManagerViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val appContext = context.applicationContext
        val files = LocalFileRepository()
        return FileManagerViewModel(
            fileRepository = files,
            remoteController = ServiceBackedRemoteController(appContext),
            safeOperations = SafeFileOperations(files),
            mediaController = WearMediaController.create(AndroidMediaStoreRepository(appContext)),
            playbackController = AndroidWearMedia3PlaybackController(appContext),
            permissionStateReader = PermissionStateReader(appContext),
        ) as T
    }
}

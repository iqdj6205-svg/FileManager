package com.sere.filemanager.wear

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sere.filemanager.core.files.LocalFileRepository
import com.sere.filemanager.core.files.SafeFileOperations

class FileManagerViewModelFactory(
    private val context: Context,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val files = LocalFileRepository()
        return FileManagerViewModel(
            fileRepository = files,
            remoteController = ServiceBackedRemoteController(context),
            safeOperations = SafeFileOperations(files),
        ) as T
    }
}

package com.sere.filemanager.phone

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sere.filemanager.core.files.AppSettingsUseCase
import com.sere.filemanager.core.files.DataStoreAppSettingsRepository
import com.sere.filemanager.core.files.LocalFileRepository
import com.sere.filemanager.core.files.SafeFileOperations
import com.sere.filemanager.core.files.StorageAccessManager
import com.sere.filemanager.core.media.AndroidMediaStoreRepository
import com.sere.filemanager.core.media.InMemoryMediaPlaybackController

class PhoneViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val appContext = context.applicationContext
        val bridge = WearBridgePhoneClient(appContext)
        val files = LocalFileRepository()
        return PhoneViewModel(
            fileRepository = files,
            wearBridgeClient = bridge,
            transferController = PhoneTransferController(appContext, bridge),
            fileOperationsController = PhoneFileOperationsController(SafeFileOperations(files)),
            mediaController = PhoneMediaController.create(AndroidMediaStoreRepository(appContext)),
            playbackController = InMemoryMediaPlaybackController(),
            settingsUseCase = AppSettingsUseCase(DataStoreAppSettingsRepository(appContext)),
            storageAccessManager = StorageAccessManager(),
            safController = PhoneSafController(appContext),
        ) as T
    }
}

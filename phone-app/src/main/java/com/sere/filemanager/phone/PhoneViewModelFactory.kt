package com.sere.filemanager.phone

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PhoneViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val appContext = context.applicationContext
        return PhoneViewModel(
            wearBridgeClient = WearBridgePhoneClient(appContext),
            transferController = PhoneTransferController(appContext),
        ) as T
    }
}

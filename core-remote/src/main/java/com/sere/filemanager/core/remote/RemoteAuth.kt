package com.sere.filemanager.core.remote

class RemoteAuth {
    fun isPinValid(expectedPin: String?, providedPin: String?): Boolean {
        if (expectedPin.isNullOrBlank()) return false
        return expectedPin == providedPin
    }
}

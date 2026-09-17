package com.sere.filemanager.core.remote

import com.sere.filemanager.core.files.PathSafety

data class RemoteValidationResult(
    val allowed: Boolean,
    val message: String? = null,
)

class RemoteRequestValidator(private val config: RemoteConfig) {
    fun validateRead(path: String, pin: String?): RemoteValidationResult {
        if (!validatePin(pin)) return RemoteValidationResult(false, RemoteWebMessages.invalidPin)
        if (PathSafety.explainIfBlocked(path) != null) return RemoteValidationResult(false, RemoteWebMessages.invalidPath)
        return RemoteValidationResult(true)
    }

    fun validateUpload(path: String, pin: String?): RemoteValidationResult {
        val read = validateRead(path, pin)
        if (!read.allowed) return read
        if (!config.allowUploads) return RemoteValidationResult(false, RemoteWebMessages.uploadsDisabled)
        return RemoteValidationResult(true)
    }

    fun validateDestructive(path: String, pin: String?): RemoteValidationResult {
        val read = validateRead(path, pin)
        if (!read.allowed) return read
        if (!config.allowDelete) return RemoteValidationResult(false, RemoteWebMessages.destructiveDisabled)
        return RemoteValidationResult(true)
    }

    private fun validatePin(pin: String?): Boolean = !config.requirePin || pin == null || pin.length >= 4
}
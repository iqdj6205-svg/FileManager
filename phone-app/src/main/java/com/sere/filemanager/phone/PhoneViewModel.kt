package com.sere.filemanager.phone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sere.filemanager.core.files.FileRepository
import com.sere.filemanager.core.files.LocalFileRepository
import com.sere.filemanager.core.model.CompanionCommand
import com.sere.filemanager.core.model.FileItemType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PhoneViewModel(
    private val protocolClient: CompanionProtocolClient = CompanionProtocolClient(),
    private val fileRepository: FileRepository = LocalFileRepository(),
) : ViewModel() {
    private val _state = MutableStateFlow(PhoneAppState())
    val state: StateFlow<PhoneAppState> = _state.asStateFlow()

    init { openPhonePath(_state.value.browser.currentPath) }

    fun openPhonePath(path: String) {
        viewModelScope.launch {
            _state.update { it.copy(browser = it.browser.copy(currentPath = path, isLoading = true, error = null)) }
            runCatching { fileRepository.list(path) }
                .onSuccess { items -> _state.update { it.copy(browser = it.browser.copy(items = items, isLoading = false)) } }
                .onFailure { error -> _state.update { it.copy(browser = it.browser.copy(isLoading = false, error = error.message ?: "Cannot open folder")) } }
        }
    }

    fun openPhoneItem(path: String, type: FileItemType) {
        if (type == FileItemType.Directory) openPhonePath(path)
        else _state.update { it.copy(statusMessage = "Selected file: $path") }
    }

    fun phoneGoUp() {
        val current = _state.value.browser.currentPath.trimEnd('/')
        val parent = current.substringBeforeLast('/', missingDelimiterValue = "/").ifBlank { "/" }
        openPhonePath(parent)
    }

    fun setRemoteUrl(url: String) {
        _state.update { it.copy(remoteUrl = url, statusMessage = if (url.isBlank()) "Remote URL empty" else "Ready to connect") }
    }

    fun startPairing() { _state.update { it.copy(statusMessage = "Pairing flow prepared. Wear Data Layer transport will be wired next.") } }
    fun startRemoteServer() = issue(protocolClient.startRemoteServer(), "Start server command prepared")
    fun stopRemoteServer() = issue(protocolClient.stopRemoteServer(), "Stop server command prepared")

    fun openRemoteManager() {
        val url = _state.value.remoteUrl
        _state.update { it.copy(statusMessage = if (url.isBlank()) "Enter watch HTTP URL first" else "Open in browser: $url") }
    }

    fun sendFiles() { _state.update { it.copy(statusMessage = "File picker and transfer queue are prepared for implementation") } }

    private fun issue(command: CompanionCommand, message: String) {
        _state.update { it.copy(statusMessage = "$message (${command.id})") }
    }
}

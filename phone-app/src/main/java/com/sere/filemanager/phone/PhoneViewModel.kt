package com.sere.filemanager.phone

import androidx.lifecycle.ViewModel
import com.sere.filemanager.core.model.CompanionCommand
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PhoneViewModel(
    private val protocolClient: CompanionProtocolClient = CompanionProtocolClient(),
) : ViewModel() {
    private val _state = MutableStateFlow(PhoneAppState())
    val state: StateFlow<PhoneAppState> = _state.asStateFlow()

    fun setRemoteUrl(url: String) {
        _state.update { it.copy(remoteUrl = url, statusMessage = if (url.isBlank()) "Remote URL empty" else "Ready to connect") }
    }

    fun startPairing() {
        _state.update { it.copy(statusMessage = "Pairing flow prepared. Wear Data Layer transport will be wired after first build.") }
    }

    fun startRemoteServer() = issue(protocolClient.startRemoteServer(), "Start server command prepared")
    fun stopRemoteServer() = issue(protocolClient.stopRemoteServer(), "Stop server command prepared")

    fun openRemoteManager() {
        val url = _state.value.remoteUrl
        _state.update { it.copy(statusMessage = if (url.isBlank()) "Enter watch HTTP URL first" else "Open in browser: $url") }
    }

    fun sendFiles() {
        _state.update { it.copy(statusMessage = "File picker and transfer queue are prepared for implementation") }
    }

    private fun issue(command: CompanionCommand, message: String) {
        _state.update { it.copy(statusMessage = "$message (${command.id})") }
    }
}

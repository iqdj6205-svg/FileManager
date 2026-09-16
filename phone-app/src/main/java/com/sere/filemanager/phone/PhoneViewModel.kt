package com.sere.filemanager.phone

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PhoneViewModel : ViewModel() {
    private val _state = MutableStateFlow(PhoneAppState())
    val state: StateFlow<PhoneAppState> = _state.asStateFlow()

    fun setRemoteUrl(url: String) {
        _state.update { it.copy(remoteUrl = url, statusMessage = if (url.isBlank()) "Remote URL empty" else "Ready to connect") }
    }

    fun startPairing() {
        _state.update { it.copy(statusMessage = "Pairing flow placeholder") }
    }

    fun sendFiles() {
        _state.update { it.copy(statusMessage = "File picker integration placeholder") }
    }
}

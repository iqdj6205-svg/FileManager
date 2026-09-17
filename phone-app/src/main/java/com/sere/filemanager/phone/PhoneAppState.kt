package com.sere.filemanager.phone

import androidx.compose.runtime.Immutable
import com.sere.filemanager.core.model.CompanionState

@Immutable
data class PhoneAppState(
    val companion: CompanionState = CompanionState(),
    val browser: PhoneFileBrowserState = PhoneFileBrowserState(),
    val remoteUrl: String = "",
    val statusMessage: String = "Phone file manager ready",
)

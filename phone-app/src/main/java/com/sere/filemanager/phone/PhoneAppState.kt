package com.sere.filemanager.phone

import androidx.compose.runtime.Immutable
import com.sere.filemanager.core.model.CompanionState

@Immutable
data class PhoneAppState(
    val companion: CompanionState = CompanionState(),
    val remoteUrl: String = "",
    val statusMessage: String = "No watch paired yet",
)

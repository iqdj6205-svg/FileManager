package com.sere.filemanager.wear

import androidx.compose.runtime.Immutable
import com.sere.filemanager.core.model.AppSettings

@Immutable
data class WearSettingsState(
    val settings: AppSettings = AppSettings(),
    val message: String? = null,
)

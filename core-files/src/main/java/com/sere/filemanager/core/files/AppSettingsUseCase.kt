package com.sere.filemanager.core.files

import com.sere.filemanager.core.model.AppSettings
import com.sere.filemanager.core.model.AppThemeMode

class AppSettingsUseCase(private val repository: AppSettingsRepository) {
    val settings = repository.settings
    suspend fun setShowHidden(enabled: Boolean) = repository.update { it.copy(showHiddenFiles = enabled) }
    suspend fun setAdvancedMode(enabled: Boolean) = repository.update { it.copy(advancedMode = enabled) }
    suspend fun setBatterySaver(enabled: Boolean) = repository.update { it.copy(batterySaver = enabled) }
    suspend fun setHaptics(enabled: Boolean) = repository.update { it.copy(hapticsEnabled = enabled) }
    suspend fun setTheme(theme: AppThemeMode) = repository.update { it.copy(theme = theme) }
    suspend fun updateRemoteSettings(transform: (com.sere.filemanager.core.model.RemoteServerSettings) -> com.sere.filemanager.core.model.RemoteServerSettings) = repository.update { it.copy(remoteServer = transform(it.remoteServer)) }
}

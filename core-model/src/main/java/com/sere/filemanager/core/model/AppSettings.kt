package com.sere.filemanager.core.model

data class AppSettings(
    val showHiddenFiles: Boolean = false,
    val advancedMode: Boolean = false,
    val batterySaver: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val theme: AppThemeMode = AppThemeMode.System,
    val remoteServer: RemoteServerSettings = RemoteServerSettings(),
)

enum class AppThemeMode { System, Dark, Light }

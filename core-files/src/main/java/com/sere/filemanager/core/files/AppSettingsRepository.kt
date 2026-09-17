package com.sere.filemanager.core.files

import com.sere.filemanager.core.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {
    val settings: Flow<AppSettings>
    suspend fun update(transform: (AppSettings) -> AppSettings)
}

package com.sere.filemanager.core.files

import com.sere.filemanager.core.model.AppSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update

class InMemoryAppSettingsRepository(initial: AppSettings = AppSettings()) : AppSettingsRepository {
    private val state = MutableStateFlow(initial)
    override val settings: Flow<AppSettings> = state
    override suspend fun update(transform: (AppSettings) -> AppSettings) { state.update(transform) }
}

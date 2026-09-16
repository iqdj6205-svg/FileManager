package com.sere.filemanager.core.files

import com.sere.filemanager.core.model.AppSettings

interface SettingsRepository {
    fun get(): AppSettings
    fun update(transform: (AppSettings) -> AppSettings): AppSettings
}

class InMemorySettingsRepository : SettingsRepository {
    private var settings = AppSettings()
    override fun get(): AppSettings = settings
    override fun update(transform: (AppSettings) -> AppSettings): AppSettings {
        settings = transform(settings)
        return settings
    }
}

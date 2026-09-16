package com.sere.filemanager.core.files

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sere.filemanager.core.model.AppSettings
import com.sere.filemanager.core.model.BatteryPolicy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.fileManagerDataStore by preferencesDataStore(name = "file_manager_settings")

class DataStoreSettingsRepository(private val context: Context) {
    private val dataStore = context.fileManagerDataStore

    val settings: Flow<AppSettings> = dataStore.data.map { prefs ->
        AppSettings(
            darkTheme = prefs[booleanPreferencesKey(SettingsKeys.DARK_THEME)] ?: true,
            haptics = prefs[booleanPreferencesKey(SettingsKeys.HAPTICS)] ?: true,
            advancedMode = prefs[booleanPreferencesKey(SettingsKeys.ADVANCED_MODE)] ?: false,
            showHiddenFiles = prefs[booleanPreferencesKey(SettingsKeys.SHOW_HIDDEN_FILES)] ?: false,
            confirmDeletes = prefs[booleanPreferencesKey(SettingsKeys.CONFIRM_DELETES)] ?: true,
            batteryPolicy = BatteryPolicy(
                autoStopServerMinutes = prefs[intPreferencesKey(SettingsKeys.REMOTE_AUTO_STOP_MINUTES)] ?: 15,
            ),
        )
    }

    suspend fun setAdvancedMode(enabled: Boolean) = setBoolean(SettingsKeys.ADVANCED_MODE, enabled)
    suspend fun setShowHiddenFiles(enabled: Boolean) = setBoolean(SettingsKeys.SHOW_HIDDEN_FILES, enabled)
    suspend fun setConfirmDeletes(enabled: Boolean) = setBoolean(SettingsKeys.CONFIRM_DELETES, enabled)
    suspend fun setHaptics(enabled: Boolean) = setBoolean(SettingsKeys.HAPTICS, enabled)
    suspend fun setDarkTheme(enabled: Boolean) = setBoolean(SettingsKeys.DARK_THEME, enabled)

    suspend fun setRemoteAutoStopMinutes(minutes: Int) {
        dataStore.edit { it[intPreferencesKey(SettingsKeys.REMOTE_AUTO_STOP_MINUTES)] = minutes.coerceIn(1, 120) }
    }

    private suspend fun setBoolean(key: String, value: Boolean) {
        dataStore.edit { it[booleanPreferencesKey(key)] = value }
    }
}

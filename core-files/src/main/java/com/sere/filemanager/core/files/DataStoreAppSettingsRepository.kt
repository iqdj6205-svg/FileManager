package com.sere.filemanager.core.files

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sere.filemanager.core.model.AppSettings
import com.sere.filemanager.core.model.AppThemeMode
import com.sere.filemanager.core.model.RemoteServerSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.appSettingsDataStore by preferencesDataStore(name = "filemanager_app_settings")

class DataStoreAppSettingsRepository(context: Context) : AppSettingsRepository {
    private val store = context.applicationContext.appSettingsDataStore

    override val settings: Flow<AppSettings> = store.data.map { p ->
        AppSettings(
            showHiddenFiles = p[Keys.SHOW_HIDDEN] ?: false,
            advancedMode = p[Keys.ADVANCED_MODE] ?: false,
            batterySaver = p[Keys.BATTERY_SAVER] ?: true,
            hapticsEnabled = p[Keys.HAPTICS] ?: true,
            theme = p[Keys.THEME]?.let { runCatching { AppThemeMode.valueOf(it) }.getOrNull() } ?: AppThemeMode.System,
            remoteServer = RemoteServerSettings(
                port = p[Keys.REMOTE_PORT] ?: 8080,
                requirePin = p[Keys.REMOTE_REQUIRE_PIN] ?: true,
                allowUploads = p[Keys.REMOTE_ALLOW_UPLOADS] ?: false,
                allowDelete = p[Keys.REMOTE_ALLOW_DELETE] ?: false,
                autoStopMinutes = p[Keys.REMOTE_AUTO_STOP] ?: 15,
                localNetworkOnly = p[Keys.REMOTE_LOCAL_ONLY] ?: true,
            ),
        )
    }

    override suspend fun update(transform: (AppSettings) -> AppSettings) {
        store.edit { p ->
            val current = AppSettings(
                showHiddenFiles = p[Keys.SHOW_HIDDEN] ?: false,
                advancedMode = p[Keys.ADVANCED_MODE] ?: false,
                batterySaver = p[Keys.BATTERY_SAVER] ?: true,
                hapticsEnabled = p[Keys.HAPTICS] ?: true,
                theme = p[Keys.THEME]?.let { runCatching { AppThemeMode.valueOf(it) }.getOrNull() } ?: AppThemeMode.System,
                remoteServer = RemoteServerSettings(
                    port = p[Keys.REMOTE_PORT] ?: 8080,
                    requirePin = p[Keys.REMOTE_REQUIRE_PIN] ?: true,
                    allowUploads = p[Keys.REMOTE_ALLOW_UPLOADS] ?: false,
                    allowDelete = p[Keys.REMOTE_ALLOW_DELETE] ?: false,
                    autoStopMinutes = p[Keys.REMOTE_AUTO_STOP] ?: 15,
                    localNetworkOnly = p[Keys.REMOTE_LOCAL_ONLY] ?: true,
                ),
            )
            val next = transform(current)
            p[Keys.SHOW_HIDDEN] = next.showHiddenFiles
            p[Keys.ADVANCED_MODE] = next.advancedMode
            p[Keys.BATTERY_SAVER] = next.batterySaver
            p[Keys.HAPTICS] = next.hapticsEnabled
            p[Keys.THEME] = next.theme.name
            p[Keys.REMOTE_PORT] = next.remoteServer.port
            p[Keys.REMOTE_REQUIRE_PIN] = next.remoteServer.requirePin
            p[Keys.REMOTE_ALLOW_UPLOADS] = next.remoteServer.allowUploads
            p[Keys.REMOTE_ALLOW_DELETE] = next.remoteServer.allowDelete
            p[Keys.REMOTE_AUTO_STOP] = next.remoteServer.autoStopMinutes
            p[Keys.REMOTE_LOCAL_ONLY] = next.remoteServer.localNetworkOnly
        }
    }

    private object Keys {
        val SHOW_HIDDEN = booleanPreferencesKey("show_hidden")
        val ADVANCED_MODE = booleanPreferencesKey("advanced_mode")
        val BATTERY_SAVER = booleanPreferencesKey("battery_saver")
        val HAPTICS = booleanPreferencesKey("haptics")
        val THEME = stringPreferencesKey("theme")
        val REMOTE_PORT = intPreferencesKey("remote_port")
        val REMOTE_REQUIRE_PIN = booleanPreferencesKey("remote_require_pin")
        val REMOTE_ALLOW_UPLOADS = booleanPreferencesKey("remote_allow_uploads")
        val REMOTE_ALLOW_DELETE = booleanPreferencesKey("remote_allow_delete")
        val REMOTE_AUTO_STOP = intPreferencesKey("remote_auto_stop")
        val REMOTE_LOCAL_ONLY = booleanPreferencesKey("remote_local_only")
    }
}

package com.sere.filemanager.core.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.sere.filemanager.core.model.AppThemeMode

private val DarkColors = darkColorScheme()
private val LightColors = lightColorScheme()

@Composable
fun FileManagerTheme(
    mode: AppThemeMode = AppThemeMode.System,
    content: @Composable () -> Unit,
) {
    val useDark = when (mode) {
        AppThemeMode.Dark -> true
        AppThemeMode.Light -> false
        AppThemeMode.System -> isSystemInDarkTheme()
    }
    MaterialTheme(colorScheme = if (useDark) DarkColors else LightColors, content = content)
}

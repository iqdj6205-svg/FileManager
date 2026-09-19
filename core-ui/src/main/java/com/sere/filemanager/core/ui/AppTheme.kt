package com.sere.filemanager.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.sere.filemanager.core.model.AppThemeMode

private val DarkColors = darkColorScheme(
    primary = AppPalette.NeonYellow,
    onPrimary = AppPalette.Black,
    primaryContainer = AppPalette.NeonYellowDim,
    onPrimaryContainer = AppPalette.Text,
    secondary = AppPalette.NeonCyan,
    onSecondary = AppPalette.Black,
    secondaryContainer = AppPalette.SurfaceElevated,
    onSecondaryContainer = AppPalette.Text,
    background = AppPalette.Black,
    onBackground = AppPalette.Text,
    surface = AppPalette.Surface,
    onSurface = AppPalette.Text,
    surfaceVariant = AppPalette.SurfaceElevated,
    onSurfaceVariant = AppPalette.TextMuted,
    error = AppPalette.Danger,
    onError = AppPalette.Black,
)

private val LightColors = lightColorScheme(
    primary = ColorTokens.LightPrimary,
    onPrimary = AppPalette.Black,
    primaryContainer = AppPalette.NeonYellowBright,
    onPrimaryContainer = AppPalette.Black,
    secondary = ColorTokens.LightSecondary,
    onSecondary = AppPalette.Black,
    background = ColorTokens.LightBackground,
    onBackground = AppPalette.Black,
    surface = ColorTokens.LightSurface,
    onSurface = AppPalette.Black,
    error = AppPalette.Danger,
    onError = AppPalette.Black,
)

private object ColorTokens {
    val LightPrimary = AppPalette.NeonYellowDim
    val LightSecondary = Color(0xFF008A99)
    val LightBackground = Color(0xFFFFFDF2)
    val LightSurface = Color(0xFFFFFFFF)
}

@Composable
fun FileManagerTheme(
    mode: AppThemeMode = AppThemeMode.System,
    content: @Composable () -> Unit,
) {
    // System mode keeps the branded dark surface so phone and Wear remain visually aligned.
    val useDark = mode != AppThemeMode.Light
    MaterialTheme(colorScheme = if (useDark) DarkColors else LightColors, content = content)
}

package com.sere.filemanager.wear

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors

/**
 * Wear Material 1.x has no darkColors() factory like phone Material.
 * Keep this small factory local to the Wear app and build its Colors directly.
 */
internal fun darkColors(
    primary: Color,
    primaryVariant: Color,
    secondary: Color,
    secondaryVariant: Color,
    background: Color,
    surface: Color,
    error: Color,
    onPrimary: Color,
    onSecondary: Color,
    onBackground: Color,
    onSurface: Color,
    onError: Color,
): Colors = Colors(
    primary = primary,
    primaryVariant = primaryVariant,
    secondary = secondary,
    secondaryVariant = secondaryVariant,
    background = background,
    surface = surface,
    error = error,
    onPrimary = onPrimary,
    onSecondary = onSecondary,
    onBackground = onBackground,
    onSurface = onSurface,
    onSurfaceVariant = onSurface,
    onError = onError,
)

package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = EcoGreenPrimary,
    onPrimary = EcoSurface,
    primaryContainer = EcoGreenLight,
    onPrimaryContainer = EcoGreenDark,
    secondary = EcoSecondary,
    onSecondary = EcoSurface,
    background = EcoBackground,
    onBackground = EcoTextDark,
    surface = EcoSurface,
    onSurface = EcoTextDark,
    surfaceVariant = EcoGreenLight,
    onSurfaceVariant = EcoGreenDark,
    error = EcoRed
)

private val DarkColorScheme = darkColorScheme(
    primary = EcoGreenMedium,
    onPrimary = EcoGreenDark,
    primaryContainer = EcoGreenDark,
    onPrimaryContainer = EcoGreenLight,
    secondary = EcoSecondary,
    onSecondary = EcoSurface,
    background = EcoGreenDark,
    onBackground = EcoGreenLight,
    surface = EcoGreenDark,
    onSurface = EcoGreenLight,
    error = EcoRed
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CyberColorScheme = darkColorScheme(
    primary = CyberNeonCyan,
    onPrimary = CyberObsidian,
    primaryContainer = CyberSurfaceDark,
    onPrimaryContainer = CyberNeonCyan,
    secondary = CyberNeonLime,
    onSecondary = CyberObsidian,
    secondaryContainer = CyberSurfaceDark,
    onSecondaryContainer = CyberNeonLime,
    tertiary = CyberMagenta,
    onTertiary = CyberObsidian,
    background = CyberObsidian,
    onBackground = CyberTextPrimary,
    surface = CyberSurfaceDark,
    onSurface = CyberTextPrimary,
    surfaceVariant = CyberCardBg,
    onSurfaceVariant = CyberTextSecondary,
    outline = CyberBorderLine,
    error = CyberErrorRed
)

@Composable
fun DevatorTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CyberColorScheme,
        typography = Typography,
        content = content
    )
}


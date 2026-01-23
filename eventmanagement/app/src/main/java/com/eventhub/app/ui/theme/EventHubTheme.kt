package com.eventhub.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Theme mode enum
enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}

// Extended color scheme for additional colors
data class ExtendedColors(
    val success: Color,
    val warning: Color,
    val info: Color,
    val cardBackground: Color,
    val textSecondary: Color
)

// Local composition for extended colors
val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        success = Color.Unspecified,
        warning = Color.Unspecified,
        info = Color.Unspecified,
        cardBackground = Color.Unspecified,
        textSecondary = Color.Unspecified
    )
}

// Light theme color scheme
private val LightColorScheme = lightColorScheme(
    primary = LightColors.primary,
    onPrimary = LightColors.onPrimary,
    secondary = LightColors.secondary,
    onSecondary = LightColors.onSecondary,
    background = LightColors.background,
    onBackground = LightColors.onBackground,
    surface = LightColors.surface,
    onSurface = LightColors.onSurface,
    error = LightColors.error,
    onError = LightColors.onError,
    surfaceVariant = LightColors.surfaceVariant,
    onSurfaceVariant = LightColors.onSurfaceVariant
)

// Dark theme color scheme
private val DarkColorScheme = darkColorScheme(
    primary = DarkColors.primary,
    onPrimary = DarkColors.onPrimary,
    secondary = DarkColors.secondary,
    onSecondary = DarkColors.onSecondary,
    background = DarkColors.background,
    onBackground = DarkColors.onBackground,
    surface = DarkColors.surface,
    onSurface = DarkColors.onSurface,
    error = DarkColors.error,
    onError = DarkColors.onError,
    surfaceVariant = DarkColors.surfaceVariant,
    onSurfaceVariant = DarkColors.onSurfaceVariant
)

// Extended colors for light theme
private val LightExtendedColors = ExtendedColors(
    success = AppColors.successGreen,
    warning = AppColors.warningOrange,
    info = AppColors.infoBlue,
    cardBackground = AppColors.cardLight,
    textSecondary = AppColors.textSecondaryLight
)

// Extended colors for dark theme
private val DarkExtendedColors = ExtendedColors(
    success = AppColors.successGreen,
    warning = AppColors.warningOrange,
    info = AppColors.infoBlue,
    cardBackground = AppColors.cardDark,
    textSecondary = AppColors.textSecondaryDark
)

@Composable
fun EventHubTheme(
    themeMode: ThemeMode = ThemeMode.LIGHT, // Default is LIGHT
    content: @Composable () -> Unit
) {
    val isSystemInDarkTheme = isSystemInDarkTheme()

    val useDarkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme
    }

    val colorScheme = if (useDarkTheme) DarkColorScheme else LightColorScheme
    val extendedColors = if (useDarkTheme) DarkExtendedColors else LightExtendedColors

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            // REMOVED: typography = Typography,  <- This was causing the error
            content = content
        )
    }
}

// Extension property to access extended colors easily
object EventHubTheme {
    val extendedColors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
}
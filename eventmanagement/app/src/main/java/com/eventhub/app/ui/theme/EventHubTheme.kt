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

// Define your color palettes
object LightColors {
    val primary = Color(0xFF6200EE)
    val onPrimary = Color(0xFFFFFFFF)
    val secondary = Color(0xFF03DAC6)
    val onSecondary = Color(0xFF000000)
    val background = Color(0xFFFFFFFF)
    val onBackground = Color(0xFF000000)
    val surface = Color(0xFFFFFFFF)
    val onSurface = Color(0xFF000000)
    val error = Color(0xFFB00020)
    val onError = Color(0xFFFFFFFF)
    val surfaceVariant = Color(0xFFF5F5F5)
    val onSurfaceVariant = Color(0xFF424242)
}

object DarkColors {
    val primary = Color(0xFFBB86FC)
    val onPrimary = Color(0xFF000000)
    val secondary = Color(0xFF03DAC6)
    val onSecondary = Color(0xFF000000)
    val background = Color(0xFF121212)
    val onBackground = Color(0xFFFFFFFF)
    val surface = Color(0xFF1E1E1E)
    val onSurface = Color(0xFFFFFFFF)
    val error = Color(0xFFCF6679)
    val onError = Color(0xFF000000)
    val surfaceVariant = Color(0xFF2C2C2C)
    val onSurfaceVariant = Color(0xFFE0E0E0)
}

object AppColors {
    val successGreen = Color(0xFF4CAF50)
    val warningOrange = Color(0xFFFF9800)
    val infoBlue = Color(0xFF2196F3)
    val cardLight = Color(0xFFFAFAFA)
    val cardDark = Color(0xFF252525)
    val textSecondaryLight = Color(0xFF757575)
    val textSecondaryDark = Color(0xFFB0B0B0)
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
    themeMode: ThemeMode = ThemeMode.LIGHT,
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
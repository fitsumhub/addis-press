package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class ThemeMode(val storageKey: String) {
    LIGHT("light"),
    DARK("dark"),
    AUTO("auto");

    companion object {
        fun fromKey(raw: String?): ThemeMode =
            entries.firstOrNull { it.storageKey == raw } ?: AUTO
    }
}

private val AddisDarkColorScheme = darkColorScheme(
    primary = ElectricBlueBright,
    onPrimary = Color.White,
    primaryContainer = ElectricBlueContainerDark,
    onPrimaryContainer = Color(0xFFDBEAFE),
    secondary = WarmAmber,
    onSecondary = BrandNavy,
    secondaryContainer = Color(0xFF3A2A0A),
    onSecondaryContainer = Color(0xFFFDE68A),
    tertiary = EmeraldAccent,
    onTertiary = BrandNavy,
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceMuted,
    outline = DarkOutline,
    outlineVariant = Color(0xFF232934),
    error = CoralRed,
    onError = Color.White
)

private val AddisLightColorScheme = lightColorScheme(
    primary = ElectricBlue,
    onPrimary = Color.White,
    primaryContainer = ElectricBlueContainerLight,
    onPrimaryContainer = BrandNavy,
    secondary = WarmAmberDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFEF3C7),
    onSecondaryContainer = Color(0xFF78350F),
    tertiary = EmeraldAccent,
    onTertiary = Color.White,
    background = LightBackground,
    onBackground = LightOnSurface,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceMuted,
    outline = LightOutline,
    outlineVariant = Color(0xFFE9EEF5),
    error = CoralRed,
    onError = Color.White
)

val AddisShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun AddisPressTheme(
    themeMode: ThemeMode = ThemeMode.AUTO,
    isAmharic: Boolean = false,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.AUTO -> isSystemInDarkTheme()
    }

    val colorScheme = if (darkTheme) AddisDarkColorScheme else AddisLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = createAppTypography(isAmharic = isAmharic),
        shapes = AddisShapes,
        content = content
    )
}

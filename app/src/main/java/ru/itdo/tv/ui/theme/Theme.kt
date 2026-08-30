package ru.itdo.tv.ui.theme

import androidx.compose.runtime.Composable
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme

private val ItdoDarkScheme = darkColorScheme(
    surface = ItdoColors.BgPrimary,
    surfaceVariant = ItdoColors.BgSecondary,
    onSurface = ItdoColors.TextPrimary,
    onSurfaceVariant = ItdoColors.TextSecondary,
    primary = ItdoColors.AccentPrimary,
    onPrimary = ItdoColors.BgPrimary,
    secondary = ItdoColors.AccentSecondary,
    background = ItdoColors.BgPrimary,
    onBackground = ItdoColors.TextPrimary,
    error = ItdoColors.AccentLike,
)

@Composable
fun ItdoTvTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ItdoDarkScheme,
        typography = ItdoTypography,
        content = content,
    )
}

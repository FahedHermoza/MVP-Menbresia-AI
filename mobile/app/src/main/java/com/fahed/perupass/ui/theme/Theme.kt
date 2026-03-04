package com.fahed.perupass.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.fahed.perupass.designsystem.MenbresiaColors

/**
 * Menbresia AI Material 3 dark color scheme.
 * Maps the design system tokens to Material's color roles.
 */
private val MenbresiaDarkColorScheme = darkColorScheme(
    primary = MenbresiaColors.Primary,
    onPrimary = Color.Black,
    background = MenbresiaColors.Background,
    onBackground = MenbresiaColors.TextPrimary,
    surface = MenbresiaColors.Surface,
    onSurface = MenbresiaColors.TextPrimary,
    surfaceVariant = MenbresiaColors.SurfaceVariant,
    onSurfaceVariant = MenbresiaColors.TextSecondary,
    error = MenbresiaColors.Error,
    onError = Color.White,
    outline = MenbresiaColors.Border
)

/**
 * Application theme for Menbresia AI.
 *
 * Always uses the dark scheme — the app design is dark-first by spec.
 * Dynamic color is intentionally disabled to preserve the dark+gold brand identity.
 */
@Composable
fun PeruPassTheme(
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MenbresiaDarkColorScheme,
        typography = Typography,
        content = content
    )
}
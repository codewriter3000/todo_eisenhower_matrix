package com.example.todo_eisenhower_matrix.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class CarbonColors(
    val background: Color,
    val layer: Color,
    val layerHover: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textPlaceholder: Color,
    val textOnColor: Color,
    val linkPrimary: Color,
    val buttonPrimary: Color,
    val buttonPrimaryHover: Color,
    val buttonSecondary: Color,
    val buttonDanger: Color,
    val borderInteractive: Color,
    val focus: Color,
    val supportError: Color,
    val supportSuccess: Color,
    val supportWarning: Color,
    val supportInfo: Color,
    val uiShell: Color,
    val onUiShell: Color,
    val headerBorder: Color
)

val CarbonWhiteColors = CarbonColors(
    background = CarbonWhite_Background,
    headerBorder = CarbonWhite_Layer03,
    layer = CarbonWhite_Layer01,
    layerHover = CarbonWhite_Layer02,
    textPrimary = CarbonWhite_TextPrimary,
    textSecondary = CarbonWhite_TextSecondary,
    textPlaceholder = Color(0xFFA8A8A8),
    textOnColor = Color.White,
    linkPrimary = CarbonBlue60,
    buttonPrimary = CarbonBlue60,
    buttonPrimaryHover = CarbonBlue70,
    buttonSecondary = Color(0xFF393939),
    buttonDanger = CarbonSupportError,
    borderInteractive = CarbonBlue60,
    focus = CarbonBlue60,
    supportError = CarbonSupportError,
    supportSuccess = CarbonSupportSuccess,
    supportWarning = CarbonSupportWarning,
    supportInfo = CarbonSupportInfo,
    uiShell = CarbonWhite_TextPrimary,
    onUiShell = Color.White,
)

val CarbonGray100Colors = CarbonColors(
    background = CarbonGray100_Background,
    headerBorder = CarbonGray100_Layer03,
    layer = CarbonGray100_Layer01,
    layerHover = CarbonGray100_Layer02,
    textPrimary = CarbonGray100_TextPrimary,
    textSecondary = CarbonGray100_TextSecondary,
    textPlaceholder = Color(0xFF707070),
    textOnColor = Color.White,
    linkPrimary = Color(0xFF78A9FF),
    buttonPrimary = CarbonBlue60,
    buttonPrimaryHover = CarbonBlue70,
    buttonSecondary = Color(0xFF6F6F6F),
    buttonDanger = Color(0xFFFA4D56),
    borderInteractive = Color(0xFF4589FF),
    focus = Color.White,
    supportError = Color(0xFFFA4D56),
    supportSuccess = Color(0xFF42BE65),
    supportWarning = CarbonSupportWarning,
    supportInfo = Color(0xFF4589FF),
    uiShell = CarbonGray100_Layer01,
    onUiShell = Color.White
)

val LocalCarbonColors = staticCompositionLocalOf { CarbonWhiteColors }
val LocalCarbonTypography = staticCompositionLocalOf { CarbonTypographyDefault }

object Carbon {
    val colors: CarbonColors
        @Composable
        get() = LocalCarbonColors.current

    val typography: CarbonTypography
        @Composable
        get() = LocalCarbonTypography.current
}

@Composable
fun CarbonTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) CarbonGray100Colors else CarbonWhiteColors

    CompositionLocalProvider(
        LocalCarbonColors provides colors,
        LocalCarbonTypography provides CarbonTypographyDefault
    ) {
        val colorScheme = if (darkTheme) {
            darkColorScheme(
                primary = CarbonBlue60,
                secondary = CarbonBlue60,
                tertiary = CarbonBlue60,
                background = colors.background,
                surface = colors.background,
                onSurface = colors.textPrimary,
                onBackground = colors.textPrimary,
                primaryContainer = CarbonBlue70,
                onPrimaryContainer = Color.White,
                outline = colors.textPlaceholder
            )
        } else {
            lightColorScheme(
                primary = CarbonBlue60,
                secondary = CarbonBlue60,
                tertiary = CarbonBlue60,
                background = colors.background,
                surface = colors.background,
                onSurface = colors.textPrimary,
                onBackground = colors.textPrimary,
                primaryContainer = CarbonBlue60,
                onPrimaryContainer = Color.White,
                outline = colors.textPlaceholder
            )
        }

        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

/**
 * A sub-theme that forces the Carbon G100 (Gray 100) palette.
 * Useful for headers or specific sections that should always be dark.
 */
@Composable
fun CarbonSubThemeG100(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalCarbonColors provides CarbonGray100Colors,
        LocalCarbonTypography provides CarbonTypographyDefault
    ) {
        content()
    }
}

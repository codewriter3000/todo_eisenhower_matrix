package com.example.todo_eisenhower_matrix.ui.carbon

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

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
    val onUiShell: Color
)

val LightCarbonColors = CarbonColors(
    background = Color(0xFFFFFFFF), // White
    layer = Color(0xFFF4F4F4), // Gray 10
    layerHover = Color(0xFFE8E8E8), // Gray 20
    textPrimary = Color(0xFF161616), // Gray 100
    textSecondary = Color(0xFF525252), // Gray 60
    textPlaceholder = Color(0xFFA8A8A8), // Gray 40
    textOnColor = Color(0xFFFFFFFF),
    linkPrimary = Color(0xFF0F62FE), // Blue 60
    buttonPrimary = Color(0xFF0F62FE), // Blue 60
    buttonPrimaryHover = Color(0xFF0353E9), // Blue 70
    buttonSecondary = Color(0xFF393939), // Gray 80
    buttonDanger = Color(0xFFDA1E28), // Red 60
    borderInteractive = Color(0xFF0F62FE),
    focus = Color(0xFF0F62FE),
    supportError = Color(0xFFDA1E28),
    supportSuccess = Color(0xFF24A148),
    supportWarning = Color(0xFFF1C21B),
    supportInfo = Color(0xFF0043CE),
    uiShell = Color(0xFF161616), // Gray 100
    onUiShell = Color(0xFFFFFFFF)
)

val DarkCarbonColors = CarbonColors(
    background = Color(0xFF161616), // Gray 100
    layer = Color(0xFF262626), // Gray 90
    layerHover = Color(0xFF393939), // Gray 80
    textPrimary = Color(0xFFF4F4F4), // Gray 10
    textSecondary = Color(0xFFC6C6C6), // Gray 30
    textPlaceholder = Color(0xFF707070), // Gray 50
    textOnColor = Color(0xFFFFFFFF),
    linkPrimary = Color(0xFF78A9FF), // Blue 40
    buttonPrimary = Color(0xFF0F62FE), // Blue 60
    buttonPrimaryHover = Color(0xFF0353E9), // Blue 70
    buttonSecondary = Color(0xFF6F6F6F), // Gray 60
    buttonDanger = Color(0xFFFA4D56), // Red 50
    borderInteractive = Color(0xFF4589FF),
    focus = Color(0xFFFFFFFF),
    supportError = Color(0xFFFA4D56),
    supportSuccess = Color(0xFF42BE65),
    supportWarning = Color(0xFFF1C21B),
    supportInfo = Color(0xFF4589FF),
    uiShell = Color(0xFF262626), // Gray 90
    onUiShell = Color(0xFFFFFFFF)
)

@Immutable
data class CarbonTypography(
    val bodyShort01: TextStyle,
    val bodyShort02: TextStyle,
    val bodyLong01: TextStyle,
    val bodyLong02: TextStyle,
    val label01: TextStyle,
    val caption01: TextStyle,
    val heading01: TextStyle,
    val heading02: TextStyle,
    val heading03: TextStyle
)

val CarbonTypographyDefault = CarbonTypography(
    bodyShort01 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp
    ),
    bodyShort02 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    bodyLong01 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodyLong02 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    label01 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    caption01 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    heading01 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 18.sp
    ),
    heading02 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),
    heading03 = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 26.sp
    )
)

val LocalCarbonColors = staticCompositionLocalOf { LightCarbonColors }
val LocalCarbonTypography = staticCompositionLocalOf { CarbonTypographyDefault }

object CarbonTheme {
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
    val colors = if (darkTheme) DarkCarbonColors else LightCarbonColors

    CompositionLocalProvider(
        LocalCarbonColors provides colors,
        LocalCarbonTypography provides CarbonTypographyDefault
    ) {
        content()
    }
}

package com.skydio.ui.components.widget.buttons

import androidx.compose.ui.graphics.Color
import com.skydio.ui.designsystem.*

/**
 * Color definition class for all buttons, both [SkydioButton] and [SkydioIconButton].
 */
data class ButtonColors(
    val backgroundColor: Color,
    val disabledBackgroundColor: Color,
    val borderColor: Color,
    val disabledBorderColor: Color,
    val contentColor: Color,
    val disabledContentColor: Color
) {
    companion object {

        fun colorsForStyle(theme: AppTheme, style: SkydioButton.Style) = when (style) {
            SkydioButton.Style.Primary -> primaryButtonColors(theme)
            SkydioButton.Style.Secondary -> secondaryButtonColors(theme)
            SkydioButton.Style.Destructive -> destructiveButtonColors(theme)
            SkydioButton.Style.Tertiary,
            SkydioButton.Style.TertiaryNoTinted-> tertiaryButtonColors(theme)
        }

        fun defaultThemeColors(
            theme: AppTheme,
            backgroundColor: Color = theme.colors.defaultWidgetBackgroundColor,
            disabledBackgroundColor: Color = theme.colors.defaultContainerBackgroundColor,
            borderColor: Color = theme.colors.defaultWidgetBackgroundColor,
            disabledBorderColor: Color = theme.colors.defaultContainerBackgroundColor,
            contentColor: Color = AppTheme.Dark.colors.primaryTextColor,
            disabledContentColor: Color = AppTheme.Dark.colors.disabledTextColor,
        ) = ButtonColors(
            backgroundColor = backgroundColor,
            disabledBackgroundColor = disabledBackgroundColor,
            borderColor = borderColor,
            disabledBorderColor = disabledBorderColor,
            contentColor = contentColor,
            disabledContentColor = disabledContentColor,
        )

        fun primaryButtonColors(theme: AppTheme) = defaultThemeColors(
            theme = theme,
            borderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            backgroundColor = Blue600,
            disabledBackgroundColor = Blue700,
        )

        fun secondaryButtonColors(theme: AppTheme) = defaultThemeColors(
            theme = theme,
            borderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            backgroundColor = Gray700,
            disabledBackgroundColor = Gray800,
        )

        fun secondaryV3ButtonColors(theme: AppTheme) = defaultThemeColors(
            theme = theme,
            borderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            backgroundColor = Gray800,
            disabledBackgroundColor = Gray900,
        )

        fun drawerButtonColors(theme: AppTheme) = defaultThemeColors(
            theme = theme,
            borderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            backgroundColor = Gray800,
            disabledBackgroundColor = Gray800,
        )

        fun destructiveButtonColors(theme: AppTheme) = defaultThemeColors(
            theme = theme,
            borderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            backgroundColor = Red600,
            disabledBackgroundColor = when (theme) {
                AppTheme.Dark -> Red800
                AppTheme.Light -> Red100
            },
        )

        fun tertiaryButtonColors(theme: AppTheme) = defaultThemeColors(
            theme = theme,
            contentColor = theme.colors.primaryTextColor,
            disabledContentColor = theme.colors.disabledTextColor,
            backgroundColor = Color.Transparent,
            disabledBackgroundColor = Color.Transparent,
            borderColor = when (theme) {
                AppTheme.Dark -> Gray600
                AppTheme.Light -> Gray400
            },
            disabledBorderColor = when (theme) {
                AppTheme.Dark -> Gray800
                AppTheme.Light -> Gray200
            },
        )

        fun noContainerButtonColors(theme: AppTheme) = defaultThemeColors(
            theme = theme,
            contentColor = theme.colors.primaryTextColor,
            disabledContentColor = theme.colors.disabledTextColor,
            backgroundColor = Color.Transparent,
            disabledBackgroundColor = Color.Transparent,
            borderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
        )

        fun overlayButtonColors(theme: AppTheme) = defaultThemeColors(
            theme = theme,
            contentColor = theme.colors.primaryTextColor,
            disabledContentColor = theme.colors.disabledTextColor,
            backgroundColor = Gray1000.copy(alpha = .8f),
            disabledBackgroundColor = Gray1000.copy(alpha = .8f),
            borderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
        )
    }
}

package com.skydio.ui.designsystem

import androidx.compose.ui.graphics.Color

/**
 * Starting point for all UI colors. Only very-commonly-used colors should live in this class, and
 * view-specific colors should be implemented as extensions to this class, taking the theme into
 * account. See the [com.skydio.ui.components.widget.SkydioButton] file for examples.
 */
data class SkydioColors(
    val parentTheme: AppTheme,
    // backgrounds/outlines
    val appBackgroundColor: Color,
    val defaultContainerBackgroundColor: Color,
    val defaultContainerBackgroundLight: Color,
    val defaultWidgetBackgroundColor: Color,
    val defaultWidgetBackgroundScrim: Color,
    val defaultWidgetOutlineColor: Color,
    val defaultWidgetOutlineScrim: Color,
    val selectedItemBackground: Color,
    val selectedItemHighlightBackground: Color,
    val selectedItemHighlightBackgroundSecondary: Color,
    // text
    val primaryTextColor: Color,
    val secondaryTextColor: Color,
    val tertiaryTextColor: Color,
    val disabledTextColor: Color,
    // dividers
    val dividerColor: Color, // used for columns and rows, different panels dividers
)

fun SkydioColors.textColor(isEnabled: Boolean) =
    if (isEnabled) primaryTextColor
    else disabledTextColor

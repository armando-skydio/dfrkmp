package com.skydio.ui.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import com.skydio.ui.designsystem.AppTheme.Companion.currentlySelectedTheme

/**
 * Get the default [AppTheme] that could be used for any Composable.
 */
@Composable
fun getAppTheme(): AppTheme =
    if (LocalInspectionMode.current) AppTheme.Dark
    else currentlySelectedTheme

/**
 * High-level styling class for colors, fonts, shapes, etc...
 * All Composable functions should get styling through an [AppTheme] instance rather than
 * directly referencing typography, colors, etc... This is to keep it as easy as possible to
 * support different themes and have a single source of truth for styles.
 */
sealed class AppTheme {

    /**
     * All themes must specify their own colors.
     */
    abstract val colors: SkydioColors

    /**
     * Themes may override shape definitions, but in general the defaults should be fine.
     */
    open val shapes: SkydioShapes by lazy {
        SkydioShapes(parentTheme = this)
    }

    /**
     * Themes may override typography definitions, but in general the defaults should be fine.
     */
    open val typography: SkydioTypography by lazy {
        SkydioTypography(parentTheme = this)
    }

    // MARK: Available Themes

    /**
     * General theme for dark mode.
     */
    object Dark : AppTheme() {

        override val colors = SkydioColors(
            parentTheme = this,
            appBackgroundColor = Gray950,
            defaultContainerBackgroundColor = Gray900,
            defaultContainerBackgroundLight = Gray850,
            defaultWidgetBackgroundColor = Gray700,
            defaultWidgetBackgroundScrim = Gray1000.copy(alpha = 0.7f),
            defaultWidgetOutlineColor = Gray600,
            defaultWidgetOutlineScrim = Gray700,
            selectedItemBackground = Blue600,
            selectedItemHighlightBackground = Yellow800,
            selectedItemHighlightBackgroundSecondary = Yellow500,
            primaryTextColor = Gray0,
            secondaryTextColor = Gray400,
            tertiaryTextColor = Gray500,
            disabledTextColor = Gray600,
            dividerColor = Gray800,
        )

    }

    /**
     * General theme for light mode.
     */
    object Light : AppTheme() {

        override val colors = SkydioColors(
            parentTheme = this,
            appBackgroundColor = Gray50,
            defaultContainerBackgroundColor = Gray150,
            defaultContainerBackgroundLight = Gray100,
            defaultWidgetBackgroundColor = Gray200,
            defaultWidgetBackgroundScrim = Gray0.copy(alpha = 0.7f),
            defaultWidgetOutlineColor = Gray300,
            defaultWidgetOutlineScrim = Gray400,
            selectedItemBackground = Blue400,
            selectedItemHighlightBackground = Yellow700,
            selectedItemHighlightBackgroundSecondary = Yellow400,
            primaryTextColor = Gray1000,
            secondaryTextColor = Gray600,
            tertiaryTextColor = Gray300,
            disabledTextColor = Gray400,
            dividerColor = Gray400,
        )

    }

    companion object {

        internal var currentlySelectedTheme: AppTheme = Dark

        fun setSelectedAppTheme(theme: AppTheme) {
            currentlySelectedTheme = theme
        }
    }
}

package com.skydio.ui.components.widget.selectors

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.widget.text.SkydioText
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme

/**
 * Generic "selected" item container with standard styling.
 */
@Composable
fun SkydioSelectorItemBox(
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    theme: AppTheme = getAppTheme(),
    colors: SkydioSelectorColors = SkydioSelectorColors.defaultThemeColors(theme),
    content: @Composable BoxScope.() -> Unit
) {
    val bgColor =
        if (isSelected && isEnabled) colors.selectedItemBackground
        else if (isSelected) colors.disabledSelectedItemBackground
        else colors.itemBackground
    val outlineColor =
        if (isSelected && isEnabled) colors.selectedItemOutline
        else Color.Transparent

    Box(
        content = content,
        contentAlignment = Alignment.Center,
        modifier = modifier
            .semantics { selected = isSelected }
            .background(color = bgColor)
            .border(1.dp, color = outlineColor))
}

/**
 * Simple selected text element, wrapped in a [SkydioSelectorItemBox].
 */
@Suppress("NAME_SHADOWING")
@Composable
fun SkydioSelectorTextItem(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    theme: AppTheme = getAppTheme(),
    textStyle: TextStyle = theme.typography.body,
    textAlpha: Float = DefaultAlpha,
    maxLines: Int = Int.MAX_VALUE,
    colors: SkydioSelectorColors = SkydioSelectorColors.defaultThemeColors(theme),
) {
    val isSelected by rememberUpdatedState(isSelected)
    val isEnabled by rememberUpdatedState(isEnabled)
    SkydioSelectorItemBox(
        modifier = modifier,
        isSelected = isSelected,
        isEnabled = isEnabled,
        colors = colors,
        theme = theme
    ) {
        SkydioText(
            text = text,
            style = textStyle.run {
                if (isEnabled) this else this.copy(color = theme.colors.disabledTextColor)
            },
            theme = theme,
            maxLines = maxLines,
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .alpha(textAlpha))
    }
}

data class SkydioSelectorColors(
    val outlineAndDivider: Color,
    val itemBackground: Color,
    val disabledSelectedItemBackground: Color,
    val selectedItemBackground: Color,
    val selectedItemOutline: Color,
) {

    companion object {
        fun defaultThemeColors(
            theme: AppTheme,
            outline: Color = theme.colors.dividerColor,
            itemBackground: Color = theme.colors.defaultWidgetBackgroundColor,
            disabledItemBackground: Color = theme.colors.defaultWidgetBackgroundColor,
            selectedItemBackground: Color = theme.colors.selectedItemBackground,
            selectedItemOutline: Color = Color.Transparent,
        ) = SkydioSelectorColors(
            outlineAndDivider = outline,
            itemBackground = itemBackground,
            disabledSelectedItemBackground = disabledItemBackground,
            selectedItemBackground = selectedItemBackground,
            selectedItemOutline = selectedItemOutline,
        )
    }

}

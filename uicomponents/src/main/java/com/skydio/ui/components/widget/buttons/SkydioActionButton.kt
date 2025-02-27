package com.skydio.ui.components.widget.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.core.Icon
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.Gray850
import com.skydio.ui.designsystem.getAppTheme
import com.skydio.ui.util.ImageSource
import com.skydio.ui.util.extension.clickableIf

val ACTION_BUTTON_HEIGHT = 32.dp
val ACTION_BUTTON_WIDTH = 54.dp
val ACTION_BUTTON_SIZE = DpSize(width = ACTION_BUTTON_WIDTH, height = ACTION_BUTTON_HEIGHT)
val ACTION_BUTTON_ICON_SIZE = DpSize(width = 16.dp, height = 16.dp)

val ActionButtonShape @Composable get() = getAppTheme().shapes.smallRoundedCorners

@Composable
fun SkydioActionButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    isDisabled: Boolean = false,
    theme: AppTheme = getAppTheme(),
    shape: Shape = ActionButtonShape,
    colors: SkydioActionButtonColors = SkydioActionButtonColors.defaultThemeColors(theme),
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val selected by rememberUpdatedState(isSelected)
    val disabled by rememberUpdatedState(isDisabled)

    val bgColor = if (disabled) colors.disabledBackground else if (selected) colors.selectedBackground else colors.background
    val borderColor = if (disabled) colors.disabledOutline else if (selected) colors.selectedOutline else colors.outline

    Box(
        contentAlignment = Alignment.Center,
        content = content,
        modifier = modifier
            .clickableIf(onClick != null) { onClick?.invoke() }
            .background(color = bgColor, shape = shape)
            .border(width = 1.dp, color = borderColor, shape = shape))
}

@Composable
fun SkydioActionButton(
    icon: ImageSource,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    theme: AppTheme = getAppTheme(),
    shape: Shape = ActionButtonShape,
    iconSize: DpSize = ACTION_BUTTON_ICON_SIZE,
    colors: SkydioActionButtonColors = SkydioActionButtonColors.defaultThemeColors(theme),
    onClick: (() -> Unit)? = null,
) {

    SkydioActionButton(
        modifier = modifier.size(ACTION_BUTTON_SIZE),
        isSelected = isSelected,
        theme = theme,
        shape = shape,
        colors = colors,
        onClick = onClick
    ) {
        Icon(
            source = icon, theme = theme, modifier = Modifier
            .size(iconSize)
            .align(Alignment.Center))
    }
}

data class SkydioActionButtonColors(
    val background: Color,
    val selectedBackground: Color,
    val disabledBackground: Color,
    val outline: Color,
    val selectedOutline: Color,
    val disabledOutline: Color,
) {

    companion object {

        fun defaultThemeColors(
            theme: AppTheme,
            background: Color = theme.colors.defaultWidgetBackgroundColor,
            selectedBackground: Color = theme.colors.selectedItemBackground,
            disabledBackground: Color = Gray850,
            outline: Color = Color.Transparent,
            selectedOutline: Color = Color.Transparent,
            disabledOutline: Color = Color.Transparent,
        ) = SkydioActionButtonColors(
            background = background,
            selectedBackground = selectedBackground,
            disabledBackground = disabledBackground,
            outline = outline,
            selectedOutline = selectedOutline,
            disabledOutline = disabledOutline)
    }

}

package com.skydio.ui.components.widget.text

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.skydio.ui.designsystem.*

/**
 * Generic box of text with a background and border color.
 * See [com.skydio.features.cockpit3.launchlandreturn.LaunchWidget] for an example.
 */
@Composable
fun ColoredTextBox(
    text: String,
    modifier: Modifier = Modifier,
    theme: AppTheme = getAppTheme(),
    colors: ColoredTextBoxColors = ColoredTextBoxColors.themeDefault(theme),
    shape: Shape = theme.shapes.smallRoundedCorners,
    textStyle: TextStyle = LocalTextStyle.current
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(color = colors.backgroundColor, shape = shape)
            .border(width = 1.dp, color = colors.borderColor, shape = shape)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            color = colors.textColor,
            style = textStyle,
            text = text)
    }
}

/**
 * Colors to be used for [ColoredTextBox], including default color palettes.
 */
data class ColoredTextBoxColors(
    val textColor: Color,
    val backgroundColor: Color,
    val borderColor: Color
) {
    companion object {

        fun themeDefault(theme: AppTheme) = ColoredTextBoxColors(
            textColor = theme.colors.primaryTextColor,
            backgroundColor = theme.colors.defaultContainerBackgroundColor,
            borderColor = theme.colors.defaultWidgetBackgroundColor
        )

        fun statusGreen(theme: AppTheme) = ColoredTextBoxColors(
            textColor = theme.colors.primaryTextColor,
            backgroundColor = when (theme) {
                AppTheme.Dark -> Green900
                AppTheme.Light -> Green300
            },
            borderColor = when (theme) {
                AppTheme.Dark -> Green500
                AppTheme.Light -> Green700
            }
        )

        fun statusYellow(theme: AppTheme) = ColoredTextBoxColors(
            textColor = theme.colors.primaryTextColor,
            backgroundColor = when (theme) {
                AppTheme.Dark -> Yellow900
                AppTheme.Light -> Yellow300
            },
            borderColor = when (theme) {
                AppTheme.Dark -> Yellow500
                AppTheme.Light -> Yellow700
            }
        )

        fun statusGray(theme: AppTheme) = ColoredTextBoxColors(
            textColor = theme.colors.primaryTextColor,
            backgroundColor = when (theme) {
                AppTheme.Dark -> Gray900
                AppTheme.Light -> Gray300
            },
            borderColor = when (theme) {
                AppTheme.Dark -> Gray500
                AppTheme.Light -> Gray700
            }
        )
    }
}

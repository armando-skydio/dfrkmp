package com.skydio.ui.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.R
import com.skydio.ui.components.core.Image
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme
import com.skydio.ui.util.DrawableImageSource
import com.skydio.ui.util.ImageSource
import com.skydio.ui.util.ThemedPreviews
import com.skydio.ui.util.extension.clickableIf

// MARK: Composable Impl

@Composable
fun SkydioNavigationHeader(
    modifier: Modifier = Modifier,
    // content
    titleText: String? = null,
    titleIcon: ImageSource? = null,
    actionIcon: ImageSource? = null,
    // styling
    theme: AppTheme = getAppTheme(),
    colors: NavigationHeaderColors = NavigationHeaderColors.defaultThemeColors(theme),
    // callbacks
    onActionClicked: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.background(color = colors.backgroundColor)
    ) {
        actionIcon?.let {
            Image(
                source = actionIcon,
                colorFilter = ColorFilter.tint(color = colors.actionIconColor),
                modifier = Modifier
                    .size(32.dp)
                    .clickableIf(onActionClicked != null) { onActionClicked?.invoke() })
            Spacer(modifier = Modifier.width(18.dp))
        }

        titleIcon?.let {
            Image(
                source = titleIcon,
                colorFilter = ColorFilter.tint(color = colors.titleIconColor),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        titleText?.let {
            Text(text = titleText, style = theme.typography.headlineLarge)
        }
    }
}

// MARK: Theming

data class NavigationHeaderColors(
    val backgroundColor: Color,
    val textColor: Color,
    val titleIconColor: Color,
    val actionIconColor: Color,
) {
    companion object {
        fun defaultThemeColors(
            theme: AppTheme,
            backgroundColor: Color = theme.colors.defaultContainerBackgroundColor,
            textColor: Color = theme.colors.primaryTextColor,
            titleIconColor: Color = theme.colors.primaryTextColor,
            actionIconColor: Color = theme.colors.primaryTextColor,
        ) = NavigationHeaderColors(
            backgroundColor = backgroundColor,
            textColor = textColor,
            titleIconColor = titleIconColor,
            actionIconColor = actionIconColor,
        )
    }
}

// MARK: Preview/Example

@Preview
@Composable
private fun SkydioNavigationHeaderPreview() {
    ThemedPreviews { theme ->
        SkydioNavigationHeader(
            modifier = Modifier.fillMaxWidth(),
            titleText = "Title Text",
            titleIcon = DrawableImageSource(R.drawable.ic_placeholder_icon),
            actionIcon = DrawableImageSource(R.drawable.ic_close),
            theme = theme
        )
    }
}

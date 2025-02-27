package com.skydio.ui.components.container

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.navigation.SkydioNavigationHeader
import com.skydio.ui.components.navigation.NavigationHeaderColors
import com.skydio.ui.util.ImageSource
import com.skydio.ui.util.ThemedPreviews
import com.skydio.ui.components.widget.TableExample
import com.skydio.ui.components.R
import com.skydio.ui.util.DrawableImageSource
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme

// MARK: Composable Impl

@Composable
fun SkydioPanel(
    modifier: Modifier = Modifier,
    // header
    headerTitle: String? = null,
    headerTitleIcon: ImageSource? = null,
    headerActionIcon: ImageSource? = null,
    onHeaderActionClicked: (() -> Unit)? = null,
    // styling
    theme: AppTheme = getAppTheme(),
    colors: PanelColors = PanelColors.defaultThemeColors(theme),
    // child content
    content: @Composable BoxScope.() -> Unit = {}
) {
    Column(
        modifier = modifier
            .background(color = colors.backgroundColor)
            .padding(12.dp)
    ) {

        val headerColors = NavigationHeaderColors.defaultThemeColors(
            theme = theme,
            backgroundColor = colors.backgroundColor,
            textColor = colors.titleTextColor,
            titleIconColor = colors.titleIconColor,
            actionIconColor = colors.actionIconColor,
        )

        SkydioNavigationHeader(
            titleText = headerTitle,
            actionIcon = headerActionIcon,
            titleIcon = headerTitleIcon,
            onActionClicked = onHeaderActionClicked,
            theme = theme,
            colors = headerColors,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(modifier = Modifier.fillMaxSize()) { content() }
    }
}

// MARK: Theming

data class PanelColors(
    val backgroundColor: Color,
    val titleTextColor: Color,
    val titleIconColor: Color,
    val actionIconColor: Color,
) {
    companion object {
        fun defaultThemeColors(
            theme: AppTheme,
            backgroundColor: Color = theme.colors.appBackgroundColor,
            titleTextColor: Color = theme.colors.primaryTextColor,
            titleIconColor: Color = theme.colors.primaryTextColor,
            actionIconColor: Color = theme.colors.primaryTextColor,
        ) = PanelColors(
            backgroundColor = backgroundColor,
            titleTextColor = titleTextColor,
            titleIconColor = titleIconColor,
            actionIconColor = actionIconColor,
        )
    }
}

// MARK: Preview/Example

@Preview
@Composable
private fun SkydioPanelPreview() {
    ThemedPreviews { theme ->
        SkydioPanel(
            headerTitle = "Title",
            headerActionIcon = DrawableImageSource(R.drawable.ic_close),
            headerTitleIcon = DrawableImageSource(R.drawable.ic_placeholder_icon),
            theme = theme,
            content = { TableExample(theme) })
    }
}

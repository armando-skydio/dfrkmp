package com.skydio.ui.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.core.Image
import com.skydio.ui.components.widget.text.SkydioText
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme
import com.skydio.ui.util.DrawableImageSource

// TODO(troy): Add support for toolbar actions to SkydioToolbar

@Composable
fun SkydioToolbar(
    title: String?,
    navigationAction: NavigationAction?,
    onToolbarNavAction: (NavigationAction) -> Unit,
    theme: AppTheme = getAppTheme()
) = SkydioToolbar(
    title = title,
    navigationAction = navigationAction,
    onToolbarNavAction = onToolbarNavAction,
    theme = theme,
    modifier = Modifier
        .height(48.dp)
        .fillMaxWidth(),
)

@Composable
fun SkydioToolbar(
    title: String?,
    navigationAction: NavigationAction?,
    modifier: Modifier,
    onToolbarNavAction: (NavigationAction) -> Unit,
    theme: AppTheme = getAppTheme()
) {
    Box(modifier = modifier.background(color = theme.colors.appBackgroundColor)) {
        if (navigationAction != null)
            Image(
                source = DrawableImageSource(navigationAction.icon),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(8.dp)
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clickable { onToolbarNavAction(navigationAction) })

        if (title != null)
            SkydioText(
                text = title,
                style = theme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.Center))
    }
}

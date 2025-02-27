package com.skydio.ui.components.widget.quickactions

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.R
import com.skydio.ui.components.core.Icon
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme
import com.skydio.ui.util.DrawableImageSource
import com.skydio.ui.util.ImageSource
import com.skydio.ui.util.ThemedPreviews

interface SkydioLinkQuickAction : SkydioQuickAction {
    fun onClick()
}

@Composable
fun SkydioQuickActionLink(
    action: SkydioLinkQuickAction,
    widgetState: SkydioQuickActionWidgetState,
    theme: AppTheme = getAppTheme(),
) = SkydioQuickActionLink(
    text = action.quickActionLabel,
    icon = action.quickActionIcon,
    onClick = { action.onClick(); widgetState.isPopoutOpen = !action.closePopoutOnQuickAction },
    theme = theme)

@Composable
fun SkydioQuickActionLink(
    text: String,
    icon: ImageSource,
    onClick: () -> Unit,
    theme: AppTheme = getAppTheme(),
) = SkydioQuickAction(
    text = text,
    icon = icon,
    onClick = onClick,
    theme = theme,
    rightContent = {
        Icon(
            theme = theme,
            source = DrawableImageSource(R.drawable.ic_quick_action_arrow),
            modifier = Modifier.size(width = 5.5.dp, height = 8.dp))
    })

@Preview
@Composable
private fun SkydioQuickActionLink() {
    ThemedPreviews { theme ->
        SkydioQuickActionLink(
            text = "Example",
            icon = DrawableImageSource(R.drawable.ic_placeholder_icon),
            theme = theme,
            onClick = { })
    }
}

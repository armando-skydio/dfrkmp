package com.skydio.ui.components.widget.quickactions

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.skydio.ui.components.R
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme
import com.skydio.ui.util.DrawableImageSource
import com.skydio.ui.util.ImageSource
import com.skydio.ui.util.ThemedPreviews

interface SkydioButtonQuickAction : SkydioQuickAction {
    fun onClick()
}

@Composable
fun SkydioQuickActionButton(
    action: SkydioButtonQuickAction,
    widgetState: SkydioQuickActionWidgetState,
    theme: AppTheme = getAppTheme(),
) = SkydioQuickActionButton(
    text = action.quickActionLabel,
    icon = action.quickActionIcon,
    onClick = { action.onClick(); widgetState.isPopoutOpen = !action.closePopoutOnQuickAction },
    theme = theme)

@Composable
fun SkydioQuickActionButton(
    text: String,
    icon: ImageSource,
    onClick: () -> Unit,
    theme: AppTheme = getAppTheme(),
) = SkydioQuickAction(
    text = text,
    icon = icon,
    onClick = onClick,
    theme = theme,
    rightContent = { })

@Preview
@Composable
private fun SkydioQuickActionButton() {
    ThemedPreviews { theme ->
        SkydioQuickActionButton(
            text = "Example",
            icon = DrawableImageSource(R.drawable.ic_placeholder_icon),
            theme = theme,
            onClick = { })
    }
}

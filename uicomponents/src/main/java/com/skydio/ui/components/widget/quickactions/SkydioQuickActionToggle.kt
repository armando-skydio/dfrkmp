package com.skydio.ui.components.widget.quickactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.R
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme
import com.skydio.ui.util.DrawableImageSource
import com.skydio.ui.util.ImageSource
import com.skydio.ui.util.ThemedPreviews

interface SkydioToggleQuickAction : SkydioQuickAction {
    val isChecked: Boolean
    val isDisabled: Boolean get() = false
    fun onCheckChanged(isChecked: Boolean)
    fun onDisabledTouched()
}

@Composable
fun SkydioQuickActionToggle(
    action: SkydioToggleQuickAction,
    widgetState: SkydioQuickActionWidgetState,
    theme: AppTheme = getAppTheme(),
    colors: SkydioQuickActionColors = SkydioQuickActionColors.defaultThemeColors(theme)
) = SkydioQuickActionToggle(
    theme = theme,
    colors = colors,
    text = action.quickActionLabel,
    icon = action.quickActionIcon,
    isChecked = action.isChecked,
    isDisabled = action.isDisabled,
    onCheckChanged = {
        action.onCheckChanged((it))
        widgetState.isPopoutOpen = !action.closePopoutOnQuickAction
    },
    onDisabledTouch = {
        action.onDisabledTouched()
    }
)

@Composable
fun SkydioQuickActionToggle(
    text: String,
    icon: ImageSource,
    isChecked: Boolean,
    isDisabled: Boolean,
    onCheckChanged: (isChecked: Boolean) -> Unit,
    onDisabledTouch: () -> Unit,
    theme: AppTheme = getAppTheme(),
    colors: SkydioQuickActionColors = SkydioQuickActionColors.defaultThemeColors(theme)
) {
    val externalIsChecked by rememberUpdatedState(isChecked)
    var internalIsChecked by remember { mutableStateOf(externalIsChecked) }
    LaunchedEffect(externalIsChecked) { internalIsChecked = externalIsChecked }

    fun onClick() {
        internalIsChecked = !internalIsChecked
        onCheckChanged(internalIsChecked)
    }

    @Composable
    fun SelectorPill() {
        val color = if (internalIsChecked) colors.selectedPill else colors.unselectedPill
        Box(
            modifier = Modifier
                .size(width = 3.dp, height = 22.dp)
                .background(color = color, shape = theme.shapes.mediumRoundedCorners))
    }

    SkydioQuickAction(
        text = text,
        icon = icon,
        onClick = ::onClick,
        theme = theme,
        rightContent = { SelectorPill() })

}

@Preview
@Composable
private fun SkydioQuickActionToggle() {
    ThemedPreviews { theme ->
        var isChecked by remember { mutableStateOf(true) }
        var isDisabled by remember { mutableStateOf( false) }
        SkydioQuickActionToggle(
            text = "Example",
            icon = DrawableImageSource(R.drawable.ic_placeholder_icon),
            isChecked = isChecked,
            isDisabled = isDisabled,
            theme = theme,
            onCheckChanged = { isChecked = it },
            onDisabledTouch = {}
        )
    }
}

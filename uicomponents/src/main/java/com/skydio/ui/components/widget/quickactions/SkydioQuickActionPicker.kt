package com.skydio.ui.components.widget.quickactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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

interface SkydioPickerQuickAction<T> : SkydioQuickAction {
    val options: List<T>
    val selectedItem: T
    val selectedIndex: Int get() = options.indexOf(selectedItem)
    fun onSelectionChanged(item: T)
}

@Composable
fun <T> SkydioQuickActionPicker(
    action: SkydioPickerQuickAction<T>,
    widgetState: SkydioQuickActionWidgetState,
    theme: AppTheme = getAppTheme(),
    colors: SkydioQuickActionColors = SkydioQuickActionColors.defaultThemeColors(theme)
) = SkydioQuickActionPicker(
    theme = theme, colors = colors,
    options = action.options,
    text = action.quickActionLabel,
    icon = action.quickActionIcon,
    selectedIndex = action.selectedIndex,
    onSelectionChanged = {
        action.onSelectionChanged((it))
        widgetState.isPopoutOpen = !action.closePopoutOnQuickAction
    })

@Composable
fun <T> SkydioQuickActionPicker(
    options: List<T>,
    text: String,
    icon: ImageSource,
    selectedIndex: Int,
    onSelectionChanged: (option: T) -> Unit,
    theme: AppTheme = getAppTheme(),
    colors: SkydioQuickActionColors = SkydioQuickActionColors.defaultThemeColors(theme)
) {
    val externalIndex by rememberUpdatedState(selectedIndex)
    var internalIndex by remember { mutableStateOf(externalIndex) }
    LaunchedEffect(externalIndex) { internalIndex = externalIndex }

    fun onClick() {
        if (options.isEmpty()) return
        internalIndex = (internalIndex + 1) % options.size
        onSelectionChanged(options[internalIndex])
    }

    @Composable
    fun SelectorPills() {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            options.forEachIndexed { index, _ ->
                val color =
                    if (index == internalIndex) colors.selectedPill
                    else colors.unselectedPill
                Box(
                    modifier = Modifier
                        .size(width = 3.dp, height = 5.dp)
                        .background(color = color, shape = theme.shapes.mediumRoundedCorners))
            }
        }
    }

    SkydioQuickAction(
        text = text,
        icon = icon,
        onClick = ::onClick,
        theme = theme,
        rightContent = { SelectorPills() })

}

@Composable
fun <T> SkydioQuickActionPicker(
    options: List<T>,
    optionToString: (T) -> String,
    icon: ImageSource,
    selectedIndex: Int,
    onSelectionChanged: (option: T) -> Unit,
    theme: AppTheme = getAppTheme(),
    colors: SkydioQuickActionColors = SkydioQuickActionColors.defaultThemeColors(theme)
) {
    val externalIndex by rememberUpdatedState(selectedIndex)
    var internalIndex by remember { mutableStateOf(externalIndex) }
    LaunchedEffect(externalIndex) { internalIndex = externalIndex }

    val text by remember {
        derivedStateOf { options.getOrNull(internalIndex)?.let(optionToString).orEmpty() }
    }

    fun onSelectionChangedInternal(option: T) {
        if (options.isEmpty()) return
        internalIndex = options.indexOf(option)
        onSelectionChanged(option)
    }

    SkydioQuickActionPicker(
        options = options,
        text = text,
        icon = icon,
        selectedIndex = internalIndex,
        theme = theme,
        colors = colors,
        onSelectionChanged = ::onSelectionChangedInternal)

}

@Preview
@Composable
private fun SkydioQuickActionPicker() {
    val options = listOf(1, 2, 3, 4)
    ThemedPreviews { theme ->
        var selectedIndex by remember { mutableStateOf(0) }
        SkydioQuickActionPicker(
            theme = theme,
            options = options,
            text = "Static",
            icon = DrawableImageSource(R.drawable.ic_placeholder_icon),
            selectedIndex = selectedIndex,
            onSelectionChanged = { selectedIndex = options.indexOf(it) })
    }
}

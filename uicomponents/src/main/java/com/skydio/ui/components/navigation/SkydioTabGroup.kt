package com.skydio.ui.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.widget.text.SkydioText
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme
import com.skydio.ui.util.ThemedPreviews

@Composable
fun <T> SkydioTabGroup(
    tabs: List<T>,
    tabToText: @Composable (T) -> String,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    theme: AppTheme = getAppTheme(),
    onTabSelectionChanged: (tab: T) -> Unit
) = SkydioTabGroup(
    tabTitles = tabs.map { tabToText(it) },
    selectedIndex = selectedIndex,
    modifier = modifier,
    theme = theme,
    onTabSelectionChanged = { onTabSelectionChanged(tabs[it]) }
)

@Composable
fun SkydioTabGroup(
    tabTitles: List<String>,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    theme: AppTheme = getAppTheme(),
    onTabSelectionChanged: (index: Int) -> Unit
) {
    val listState = rememberLazyListState()

    val externalSelectedIndex by rememberUpdatedState(selectedIndex)
    var internalSelectedIndex by remember { mutableStateOf(externalSelectedIndex) }
    LaunchedEffect(externalSelectedIndex) { internalSelectedIndex = externalSelectedIndex }

    fun updateSelectedIndex(index: Int) {
        internalSelectedIndex = index
        onTabSelectionChanged(internalSelectedIndex)
    }

    LazyRow(state = listState, modifier = modifier) {
        itemsIndexed(tabTitles) { index, title ->
            val isSelected = internalSelectedIndex == index
            SkydioTextTab(
                text = title,
                isSelected = isSelected,
                theme = theme,
                onClick = { updateSelectedIndex(index) }
            )
        }
    }
}

@Suppress("NAME_SHADOWING")
@Composable
private fun SkydioTextTab(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    theme: AppTheme = getAppTheme(),
    textStyle: TextStyle = theme.typography.bodyLarge,
    colors: TabGroupColors = TabGroupColors.defaultThemeColors(theme),
    onClick: () -> Unit
) {
    val density = LocalDensity.current
    val isSelected by rememberUpdatedState(isSelected)
    val textColor by remember {
        derivedStateOf {
            if (isSelected) colors.selectedTabTextColor
            else colors.unselectedTabTextColor
        }
    }
    val underlineColor by remember {
        derivedStateOf {
            if (isSelected) colors.selectedTabUnderlineColor
            else Color.Transparent
        }
    }

    Column(modifier = modifier.clickable { onClick() }) {
        var textWidth by remember { mutableStateOf(0.dp) }
        SkydioText(
            text = text,
            style = textStyle,
            color = textColor,
            modifier = Modifier
                .onSizeChanged { with(density) { textWidth = it.width.toDp() } }
                .padding(horizontal = 8.dp)
        )
        Box(modifier = Modifier
            .height(2.dp)
            .width(textWidth)
            .background(color = underlineColor))
    }
}

// MARK: Theming

data class TabGroupColors(
    val selectedTabUnderlineColor: Color,
    val unselectedTabTextColor: Color,
    val selectedTabTextColor: Color,
) {
    companion object {
        fun defaultThemeColors(
            theme: AppTheme,
            selectedTabUnderlineColor: Color = theme.colors.selectedItemBackground,
            unselectedTabTextColor: Color = theme.colors.secondaryTextColor,
            selectedTabTextColor: Color = theme.colors.primaryTextColor,
        ) = TabGroupColors(
            selectedTabUnderlineColor = selectedTabUnderlineColor,
            unselectedTabTextColor = unselectedTabTextColor,
            selectedTabTextColor = selectedTabTextColor,
        )
    }
}

// MARK: Preview/Example

@Preview
@Composable
private fun SkydioIconTabPreview() {
    ThemedPreviews { theme ->
        val tabs = listOf("Tab 1", "Tab 2", "Tab 3")
        SkydioTabGroup(tabTitles = tabs, selectedIndex = 1, theme = theme) {}
    }
}

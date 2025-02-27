package com.skydio.ui.components.widget.selectors

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.widget.text.SkydioText
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme

/**
 * Segment list control selector, with a fixed number of columns. The content of each cell can be
 * dynamically provided on a per-item basis via the [itemContent] composable.
 */
@Suppress("NAME_SHADOWING")
@Composable
fun <T> SkydioListControl(
    items: List<T>,
    itemToString: (T) -> Pair<String,String>,
    modifier: Modifier = Modifier,
    selectedIndex: Int = 0,
    theme: AppTheme = getAppTheme(),
    textStyle: TextStyle = theme.typography.bodyLarge,
    colors: SkydioSelectorColors = SkydioSelectorColors.defaultThemeColors(theme),
    onItemSelected: (T) -> Unit,
) {
    val items by rememberUpdatedState(items)
    val selectedIndex by rememberUpdatedState(selectedIndex)
    val strings by remember { derivedStateOf { items.map(itemToString) } }

    SkydioTextListControl(
            strings = strings,
            modifier = modifier,
            selectedIndex = selectedIndex,
            onItemSelected = { items.getOrNull(it)?.let(onItemSelected) },
            theme = theme,
            textStyle = textStyle,
            colors = colors
    )
}

@Suppress("NAME_SHADOWING")
@Composable
fun SkydioTextListControl(
        strings: List<Pair<String,String>>,
        modifier: Modifier = Modifier,
        selectedIndex: Int = 0,
        theme: AppTheme = getAppTheme(),
        textStyle: TextStyle = theme.typography.bodyLarge,
        colors: SkydioSelectorColors = SkydioSelectorColors.defaultThemeColors(theme),
        onItemSelected: (Int) -> Unit,
) {
    val items by rememberUpdatedState(strings)
    val selectedIndex by rememberUpdatedState(selectedIndex)

    SkydioSegmentControl(
            items = items,
            numColumns = 1,
            selectedIndex = selectedIndex,
            onItemSelected = { onItemSelected(items.indexOf(it)) },
            colors = colors,
            modifier = modifier
    ) { item ->
        Column(
                verticalArrangement = Arrangement.spacedBy(0.dp),
                modifier = modifier.padding(horizontal = 16.dp)
        ) {
            SkydioText(
                    text = item.first,
                    style = theme.typography.bodyLarge,
                    textAlign = TextAlign.Left,
                    theme = theme,
                    maxLines = 5,
                    modifier = modifier.padding(top = 15.dp))
            SkydioText(
                    text = item.second,
                    style = theme.typography.body,
                    textAlign = TextAlign.Left,
                    color = theme.colors.secondaryTextColor,
                    theme = theme,
                    maxLines = 10,
                    modifier = modifier.padding(bottom = 15.dp))

        }
    }
}

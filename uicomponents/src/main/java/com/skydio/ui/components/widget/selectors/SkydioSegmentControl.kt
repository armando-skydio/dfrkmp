package com.skydio.ui.components.widget.selectors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.container.SkydioFixedGrid
import com.skydio.ui.components.widget.text.SkydioText
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme
import kotlin.math.roundToInt

/**
 * Segment control selector, with a fixed number of columns. The content of each cell can be
 * dynamically provided on a per-item basis via the [itemContent] composable. If you want a segment
 * control that has a dynamic number of columns base don content size (great for localization)
 * then use [SkydioDynamicSegmentControl].
 */
@Suppress("NAME_SHADOWING")
@Composable
fun <T> SkydioSegmentControl(
    items: List<T>,
    selectedIndex: Int,
    numColumns: Int,
    modifier: Modifier = Modifier,
    theme: AppTheme = getAppTheme(),
    colors: SkydioSelectorColors = SkydioSelectorColors.defaultThemeColors(theme),
    onItemSelected: (T) -> Unit,
    itemContent: @Composable BoxScope.(item: T) -> Unit
) {
    val items by rememberUpdatedState(items)
    val numColumns by rememberUpdatedState(numColumns)

    val externalSelectedIndex by rememberUpdatedState(selectedIndex)
    var internalSelectedIndex by remember { mutableStateOf(externalSelectedIndex) }

    LaunchedEffect(externalSelectedIndex) {
        internalSelectedIndex = externalSelectedIndex
    }

    fun updateSelected(index: Int, item: T) {
        onItemSelected(item)
        internalSelectedIndex = index
    }

    SkydioFixedGrid(
        items = items,
        columns = numColumns,
        allowLastRowToFill = false,
        modifier = modifier
            .background(color = colors.outlineAndDivider, shape = theme.shapes.smallRoundedCorners)
            .padding(0.5.dp)
            .clip(shape = theme.shapes.smallRoundedCorners),
    ) { index, item ->
        SkydioSelectorItemBox(
            isSelected = internalSelectedIndex == index,
            modifier = Modifier
                .fillMaxSize()
                .padding(0.5.dp)
                .clickable { updateSelected(index, item) },
            colors = colors
        ) {
            itemContent(item)
        }
    }
}

/**
 * Variant of [SkydioSegmentControl] that will use the minimum number of columns to fit the largest
 * single content item. In other words, it will dynamically calculate the number of columns to
 * best fit the UI.
 */
@Suppress("NAME_SHADOWING")
@Composable
fun <T> SkydioDynamicSegmentControl(
    items: List<T>,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    theme: AppTheme = getAppTheme(),
    colors: SkydioSelectorColors = SkydioSelectorColors.defaultThemeColors(theme),
    onItemSelected: (T) -> Unit,
    itemContent: @Composable BoxScope.(item: T) -> Unit
) {
    val items by rememberUpdatedState(items)
    val selectedIndex by rememberUpdatedState(selectedIndex)

    var overallWidth by remember { mutableIntStateOf(0) }
    var largestItemWidth by remember { mutableIntStateOf(0) }

    val numColumns by remember {
        derivedStateOf {
            if (items.isEmpty()) return@derivedStateOf 1
            val maxThatCanFit = overallWidth / maxOf(largestItemWidth, 1)
            var target = minOf(maxThatCanFit, items.size).coerceAtLeast(1)
            val numItemsLastRow = items.size.mod(target)
            val numColumnsForHalf = (target / 2f).roundToInt()
            // if the last row would be more than half empty, then adjust columns to maximize fill
            if (numItemsLastRow in 1 until numColumnsForHalf) target = numColumnsForHalf
            target
        }
    }

    val isMeasured by remember {
        derivedStateOf {
            items.isNotEmpty() && overallWidth > 0 && largestItemWidth > 0
        }
    }

    // Force recalculation of largestItemWidth if the number of items changes
    var lastNumItems by remember { mutableIntStateOf(0) }
    if (items.size != lastNumItems) {
        lastNumItems = items.size
        largestItemWidth = 0
    }

    SkydioSegmentControl(
        items = items,
        numColumns = if (isMeasured) numColumns else 1,
        selectedIndex = selectedIndex,
        onItemSelected = onItemSelected,
        colors = colors,
        modifier = modifier
            .onSizeChanged { if (items.isNotEmpty()) overallWidth = it.width }
            .let { if (isMeasured) it else it.height(0.dp) }
    ) { item ->
        Box(modifier = Modifier
            .onSizeChanged { largestItemWidth = maxOf(largestItemWidth, it.width) }
        ) {
            itemContent(item)
        }
    }
}


/**
 * Variant of [SkydioDynamicSegmentControl] which only displays text. Since this is a
 * [SkydioDynamicSegmentControl], the number of columns will be whatever best fits the UI.
 */
@Suppress("NAME_SHADOWING")
@Composable
fun SkydioTextSegmentControl(
    strings: List<String>,
    modifier: Modifier = Modifier,
    selectedIndex: Int = 0,
    theme: AppTheme = getAppTheme(),
    textStyle: TextStyle = theme.typography.body,
    colors: SkydioSelectorColors = SkydioSelectorColors.defaultThemeColors(theme),
    onItemSelected: (Int) -> Unit,
) {
    val items by rememberUpdatedState(strings)
    val selectedIndex by rememberUpdatedState(selectedIndex)

    SkydioDynamicSegmentControl(
        items = items,
        selectedIndex = selectedIndex,
        modifier = modifier,
        theme = theme,
        colors = colors,
        onItemSelected = { onItemSelected(items.indexOf(it)) }
    ) { item ->
        SkydioText(
            text = item,
            style = textStyle,
            textAlign = TextAlign.Center,
            theme = theme,
            maxLines = 1,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(4.dp))
    }
}

/**
 * Variant of [SkydioTextSegmentControl] which adapts arbitrary items to a string
 * via [itemToString]. Since this is a [SkydioDynamicSegmentControl], the number of columns will
 * be whatever best fits the UI.
 */
@Suppress("NAME_SHADOWING")
@Composable
fun <T> SkydioTextSegmentControl(
    items: List<T>,
    itemToString: (T) -> String,
    modifier: Modifier = Modifier,
    selectedIndex: Int = 0,
    theme: AppTheme = getAppTheme(),
    textStyle: TextStyle = theme.typography.body,
    colors: SkydioSelectorColors = SkydioSelectorColors.defaultThemeColors(theme),
    onItemSelected: (T) -> Unit,
) {
    val items by rememberUpdatedState(items)
    val selectedIndex by rememberUpdatedState(selectedIndex)
    val strings by remember { derivedStateOf { items.map(itemToString) } }

    SkydioTextSegmentControl(
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
fun <T> SkydioTextSegmentControlWithDetail(
    items: List<T>,
    itemToString: (T) -> Pair<String, String>,
    modifier: Modifier = Modifier,
    selectedIndex: Int = 0,
    theme: AppTheme = getAppTheme(),
    textStyle: TextStyle = theme.typography.body,
    colors: SkydioSelectorColors = SkydioSelectorColors.defaultThemeColors(theme),
    onItemSelected: (T) -> Unit,
) {
    val items by rememberUpdatedState(items)
    val selectedIndex by rememberUpdatedState(selectedIndex)
    val strings by remember { derivedStateOf { items.map(itemToString) } }

    Column {
        SkydioTextSegmentControl(
            strings = strings.map { it.first },
            modifier = modifier,
            selectedIndex = selectedIndex,
            onItemSelected = { items.getOrNull(it)?.let(onItemSelected) },
            theme = theme,
            textStyle = textStyle,
            colors = colors
        )
        SkydioText(
            text = strings[selectedIndex].second,
            style = theme.typography.body,
            theme = theme,
            modifier = Modifier
                .padding(4.dp))
    }
}

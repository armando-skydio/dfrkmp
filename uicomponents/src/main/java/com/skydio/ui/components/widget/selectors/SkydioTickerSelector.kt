package com.skydio.ui.components.widget.selectors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme
import com.skydio.ui.util.ThemedPreviews
import com.skydio.ui.util.extension.noRippleClickable
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun <T> SkydioTickerSelector(
    items: List<T>,
    itemToString: (T) -> String,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    selectedIndex: Int = 0,
    theme: AppTheme = getAppTheme(),
    textStyle: TextStyle = theme.typography.bodyLarge,
    colors: SkydioSelectorColors = SkydioSelectorColors.defaultThemeColors(theme),
    onItemSelected: (T) -> Unit,
) {
    val itemsState = rememberUpdatedState(newValue = items)
    SkydioStringTickerSelector(
        items = itemsState.value.map(itemToString),
        modifier = modifier,
        isEnabled = isEnabled,
        selectedIndex = selectedIndex,
        onItemSelected = { itemsState.value.getOrNull(it)?.let(onItemSelected) },
        theme = theme,
        textStyle = textStyle,
        colors = colors
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SkydioStringTickerSelector(
    items: List<String>,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    selectedIndex: Int = 0,
    theme: AppTheme = getAppTheme(),
    textStyle: TextStyle = theme.typography.footnote,
    colors: SkydioSelectorColors = SkydioSelectorColors.defaultThemeColors(theme),
    onItemSelected: (Int) -> Unit,
) {
    val itemsState by rememberUpdatedState(items)
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val externalSelectedIndex by rememberUpdatedState(selectedIndex)
    var internalSelectedIndex by remember { mutableStateOf(externalSelectedIndex) }

    var isScrolling by remember { mutableStateOf(false) }
    var isAutoScrolling = remember { false }

    LaunchedEffect(externalSelectedIndex) {
        if (!isScrolling && !isAutoScrolling) internalSelectedIndex = externalSelectedIndex
    }

    fun calcAlpha(index: Int): Float {
        val deviation = abs(index - internalSelectedIndex)
        return ((1f / (1 shl deviation)) + 0.1f).coerceIn(0.1f, 1f)
    }

    with(LocalDensity.current) {
        BoxWithConstraints(
            modifier = Modifier
                .height(32.dp)
                .background(
                    color = colors.outlineAndDivider,
                    shape = theme.shapes.smallRoundedCorners)
                .clip(shape = theme.shapes.smallRoundedCorners)
                .pointerInteropFilter { isEnabled.not() }
                .then(modifier)
        ) {
            val rowWidth = constraints.maxWidth.toDp()
            var minItemWidth by remember { mutableStateOf(1.dp) }
            val spacerWidth by remember { derivedStateOf { (rowWidth / 2) - (minItemWidth / 2) } }

            LaunchedEffect(listState.isScrollInProgress) {
                if (listState.isScrollInProgress != isScrolling)
                    isScrolling = listState.isScrollInProgress
            }

            fun snapToSelected(animate: Boolean) {
                coroutineScope.launch {
                    val scrollToIndex = maxOf(internalSelectedIndex + 1, 0)
                    val scrollOffset = -(spacerWidth).toPx().toInt()
                    isAutoScrolling = true
                    if (animate) listState.animateScrollToItem(scrollToIndex, scrollOffset)
                    else listState.scrollToItem(scrollToIndex, scrollOffset)
                    isAutoScrolling = false
                }
            }

            var lastNotifiedIndex = -1
            fun notifyIndexChanged(newIndex: Int) {
                if (lastNotifiedIndex == newIndex) return
                else lastNotifiedIndex = newIndex
                internalSelectedIndex = newIndex
                onItemSelected(newIndex)
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(1.dp)
                    .background(
                        color = colors.itemBackground,
                        shape = theme.shapes.smallRoundedCorners)
            )

            LazyRow(
                state = listState,
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .fillMaxSize()
            ) {
                item { Spacer(modifier = Modifier.width(spacerWidth)) }

                itemsIndexed(itemsState) { index, item ->
                    val isSelected = index == internalSelectedIndex
                    SkydioSelectorTextItem(
                        text = item,
                        isSelected = isSelected,
                        isEnabled = isEnabled,
                        textStyle = textStyle,
                        textAlpha = calcAlpha(index),
                        colors = colors,
                        theme = theme,
                        modifier = Modifier
                            .let { if (isSelected) it else it.padding(vertical = 1.dp) }
                            .clip(shape = theme.shapes.smallRoundedCorners)
                            .defaultMinSize(minWidth = minItemWidth)
                            .fillMaxHeight()
                            .onSizeChanged { minItemWidth = maxOf(it.width.toDp(), minItemWidth) }
                            .noRippleClickable(enabled = isEnabled) { notifyIndexChanged(index) }
                    )
                }

                item { Spacer(modifier = Modifier.width(spacerWidth)) }
            }

            snapToSelected(animate = false)

            // Update the selected index while scrolling
            LaunchedEffect(listState) {
                snapshotFlow { listState.layoutInfo.visibleItemsInfo }.collect { infoList ->
                    val center = listState.layoutInfo.viewportEndOffset / 2
                    val actualIndex = infoList.firstOrNull { info ->
                        info.offset < center && (info.offset + info.size) > center
                    }?.index ?: return@collect

                    notifyIndexChanged(actualIndex - 1)
                }
            }

            // Snap to the selected item when scrolling stops
            LaunchedEffect(isScrolling) {
                if (!isScrolling) snapToSelected(animate = true)
            }

            // Snap to the selected item when initial index is set
            LaunchedEffect(internalSelectedIndex) {
                if (!isScrolling) snapToSelected(animate = false)
            }
        }
    }
}

@Preview
@Composable
private fun Test() {
    ThemedPreviews { theme ->
        SkydioStringTickerSelector(
            items = (1 until 11).map { it.toString() },
            theme = theme
        ) {}
    }
}

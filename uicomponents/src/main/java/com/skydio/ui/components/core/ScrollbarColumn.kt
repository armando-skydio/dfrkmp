package com.skydio.ui.components.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme
import kotlin.math.roundToInt

/**
 * Wrapper around [Column] that is always scrollable (if needed) and will show a scrollbar.
 * @param alwaysShowScrollbarWhenAvailable if true, the scrollbar shows when not actively scrolling.
 */
@Composable
fun ScrollbarColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    alwaysShowScrollbarWhenAvailable: Boolean = true,
    theme: AppTheme = getAppTheme(),
    scrollbarColor: Color = theme.colors.defaultWidgetBackgroundColor,
    content: @Composable ColumnScope.() -> Unit
) {

    val density = LocalDensity.current
    val scrollState = rememberScrollState()

    var availableSize by remember { mutableStateOf(IntSize.Zero) }
    var measuredSize by remember { mutableStateOf(IntSize.Zero) }

    val scrollbarPercentage by remember {
        derivedStateOf {
            minOf(1f, 0.05f +
                ((if (measuredSize.height == 0) 0f
                else availableSize.height.toFloat() / measuredSize.height.toFloat()))
            )
        }
    }

    val scrollbarHeightDp by remember {
        derivedStateOf {
            if (availableSize.height == 0 || scrollbarPercentage == 0f) 0.dp
            else with(density) { (availableSize.height.toFloat() * scrollbarPercentage).toDp() }
        }
    }

    val scrollbarOffsetDp by remember {
        derivedStateOf {
            val ratio = scrollState.value / scrollState.maxValue.toFloat()
            val scrollbarHeightFloat = (availableSize.height * scrollbarPercentage)
            if (ratio.isNaN() || scrollbarHeightFloat.isNaN()) 0.dp
            else {
                val scrollbarHeightInt = scrollbarHeightFloat.roundToInt()
                val offset = ((availableSize.height - scrollbarHeightInt) * ratio).roundToInt()
                with(density) { offset.toDp() }
            }

        }
    }

    val canShowScrollbar by remember {
        derivedStateOf {
            scrollbarPercentage < 1f &&
                (scrollState.canScrollBackward || scrollState.canScrollForward)
        }
    }

    val shouldShowScrollbar by remember {
        derivedStateOf {
            alwaysShowScrollbarWhenAvailable || scrollState.isScrollInProgress
        }
    }

    val doShowScrollbar by remember {
        derivedStateOf {
            canShowScrollbar && shouldShowScrollbar
        }
    }

    Box(Modifier.onSizeChanged { availableSize = it }) {

        Column(
            modifier = modifier
                .verticalScroll(scrollState)
                .onSizeChanged { measuredSize = it },
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            content = content)

        if (doShowScrollbar) Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .width(3.dp)
                .height(scrollbarHeightDp)
                .offset(y = scrollbarOffsetDp)
                .background(
                    color = scrollbarColor,
                    shape = theme.shapes.smallRoundedCorners)) {
        }
    }
}

package com.skydio.ui.components.widget.sliders

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydio.ui.util.ThemedPreviews
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

// TODO(troy): Polish this (filled bleeds over thumb when dragging, right thumb squishes, etc...)

private val thumbSize = 24.dp
private val thumbOutlineWidth = 4.dp
private val trackHeight = 4.dp

// MARK: Composable Impl

@Composable
fun SkydioRangeSlider(
    modifier: Modifier = Modifier,
    // state
    range: SliderRange = 0f..100f,
    value: SliderRange = range,
    minSelectionSize: Float = 0f,
    stepSize: Float = Float.NEGATIVE_INFINITY,
    isEnabled: Boolean = true,
    // styling
    theme: AppTheme = getAppTheme(),
    colors: RangeSliderColors = RangeSliderColors.defaultThemeColors(theme),
    // callbacks
    onRangeUpdated: (SliderRange) -> Unit
) {
    val colorsState = rememberUpdatedState(colors)

    val isEnabledState = rememberUpdatedState(isEnabled)
    val callback by rememberUpdatedState(newValue = onRangeUpdated)

    val thumbColorState = remember {
        derivedStateOf {
            if (isEnabledState.value) colorsState.value.thumbColor
            else colorsState.value.disabledThumbColor
        }
    }

    val thumbOutlineColorState = remember {
        derivedStateOf {
            if (isEnabledState.value) colorsState.value.thumbOutlineColor
            else colorsState.value.disabledThumbOutlineColor
        }
    }

    val trackBrushState = remember {
        derivedStateOf {
            if (isEnabledState.value) colorsState.value.trackGradient
            else colorsState.value.disabledTrackGradient
        }
    }

    val filledTrackBrushState = remember {
        derivedStateOf {
            if (isEnabledState.value) colorsState.value.filledTrackGradient
            else colorsState.value.disabledFilledTrackGradient
        }
    }

    @Composable
    fun Thumb(onThumbDragged: (Float) -> Unit) = Box(
        modifier = Modifier
            .size(thumbSize)
            .background(
                color = thumbColorState.value,
                shape = CircleShape)
            .border(
                width = thumbOutlineWidth,
                color = thumbOutlineColorState.value,
                shape = CircleShape)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    onThumbDragged(dragAmount.x)
                    change.consumeAllChanges()
                }
            }
    )

    with(LocalDensity.current) {

        val externalValue by rememberUpdatedState(value)

        val scope = rememberCoroutineScope()
        var isUserDragging by remember { mutableStateOf(false) }
        var dragEndJob: Job? by remember { mutableStateOf(null) }

        // TODO(troy): We can track range slider drags better, but this should work for now
        fun updateIsDragging() {
            dragEndJob?.cancel()
            isUserDragging = true
            dragEndJob = scope.launch {
                delay(250L)
                if (isActive) isUserDragging = false
            }
        }

        BoxWithConstraints(modifier = modifier) {
            val rangeTotal = range.endInclusive - range.start

            val initialTrackWidth = constraints.maxWidth - ((thumbSize.toPx() - 1.dp.toPx()) * 2)
            var measuredTrackWidth by remember { mutableStateOf(initialTrackWidth) }
            val minActiveTrackWidth by remember {
                derivedStateOf { measuredTrackWidth * (minSelectionSize / rangeTotal) }
            }

            val externalStartOffset by remember {
                derivedStateOf {
                    initialTrackWidth * (externalValue.start / rangeTotal)
                }
            }

            val externalEndOffset by remember {
                derivedStateOf {
                    initialTrackWidth * ((externalValue.endInclusive - rangeTotal) / rangeTotal)
                }
            }

            var internalStartOffset by remember { mutableStateOf(externalStartOffset) }
            LaunchedEffect(externalStartOffset) {
                if (!isUserDragging) internalStartOffset = externalStartOffset
            }

            var internalEndOffset by remember { mutableStateOf(externalEndOffset) }
            LaunchedEffect(externalEndOffset) {
                if (!isUserDragging) internalEndOffset = externalEndOffset
            }

            fun updateRange() {
                updateIsDragging()
                val startProportion = internalStartOffset / measuredTrackWidth
                val start = (rangeTotal * startProportion) + range.start
                val endProportion = internalEndOffset / measuredTrackWidth
                val end = range.endInclusive - (rangeTotal * endProportion)
                val selectedRange = start.roundToStepSize(stepSize)..end.roundToStepSize(stepSize)
                callback(selectedRange)
            }

            fun validateBounds(
                startOffset: Float = internalStartOffset,
                endOffset: Float = internalEndOffset
            ): Boolean {
                val isOnTrack = startOffset >= 0 && endOffset >= 0
                val isNoOverlapping = startOffset + endOffset <= measuredTrackWidth
                val isMinRespected =
                    measuredTrackWidth - startOffset - endOffset >= minActiveTrackWidth
                return isOnTrack && isMinRespected && isNoOverlapping
            }

            fun updateStartThumb(dx: Float) {
                val newStart = internalStartOffset + dx
                if (validateBounds(startOffset = newStart)) internalStartOffset = newStart
                updateRange()
            }

            fun updateEndThumb(dx: Float) {
                val newEnd = internalEndOffset - dx
                if (validateBounds(endOffset = newEnd)) internalEndOffset = newEnd
                updateRange()
            }

            Canvas(modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight)
                .align(Alignment.Center)
                .padding(horizontal = thumbSize - 1.dp)
                .onSizeChanged { measuredTrackWidth = it.width.toFloat() }
            ) {
                val trackY = (size.height - trackHeight.toPx()) / 2
                drawRoundRect(
                    brush = filledTrackBrushState.value,
                    topLeft = Offset(0f, trackY),
                    size = Size(size.width, trackHeight.toPx()),
                    cornerRadius = CornerRadius(8f, 8f)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            ) {

                Canvas(modifier = Modifier
                    .width(internalStartOffset.toDp())
                    .height(trackHeight)
                    .padding(start = thumbSize - 1.dp)
                ) {
                    val trackY = (size.height - trackHeight.toPx()) / 2
                    drawRoundRect(
                        brush = trackBrushState.value,
                        topLeft = Offset(0f, trackY),
                        size = Size(size.width, trackHeight.toPx()),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                }

                Thumb(onThumbDragged = ::updateStartThumb)

                Spacer(modifier = Modifier.weight(1f))

                Thumb(onThumbDragged = ::updateEndThumb)

                Canvas(modifier = Modifier
                    .width(internalEndOffset.toDp())
                    .height(trackHeight)
                    .padding(end = thumbSize - 1.dp)
                ) {
                    val trackY = (size.height - trackHeight.toPx()) / 2
                    drawRoundRect(
                        brush = trackBrushState.value,
                        topLeft = Offset(0f, trackY),
                        size = Size(size.width, trackHeight.toPx()),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                }

            }

        }
    }
}

@Composable
fun SkydioRangeSliderWithValue(
    modifier: Modifier = Modifier,
    // state
    range: SliderRange = 0f..100f,
    value: SliderRange = range,
    minSelectionSize: Float = 0f,
    stepSize: Float = Float.NEGATIVE_INFINITY,
    isEnabled: Boolean = true,
    // styling
    theme: AppTheme = getAppTheme(),
    textStyle: TextStyle = theme.typography.body,
    colors: RangeSliderColors = RangeSliderColors.defaultThemeColors(theme),
    // callbacks
    onRangeUpdated: (SliderRange) -> Unit
) {
    val externalValue by rememberUpdatedState(value)
    var internalValue by remember { mutableStateOf(externalValue) }

    LaunchedEffect(externalValue) { internalValue = externalValue }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        SkydioRangeSlider(
            range = range,
            value = value,
            minSelectionSize = minSelectionSize,
            stepSize = stepSize,
            isEnabled = isEnabled,
            theme = theme,
            colors = colors,
            onRangeUpdated = {
                internalValue = it
                onRangeUpdated(internalValue)
            },
            modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = internalValue.start.toString(), style = textStyle)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = internalValue.endInclusive.toString(), style = textStyle)
    }
}

@Composable
fun SkydioRangeSliderWithInput(
    modifier: Modifier = Modifier,
    // state
    range: SliderRange = 0f..100f,
    value: SliderRange = range,
    minSelectionSize: Float = 0f,
    stepSize: Float = Float.NEGATIVE_INFINITY,
    isEnabled: Boolean = true,
    // styling
    theme: AppTheme = getAppTheme(),
    inputShape: Shape = theme.shapes.mediumRoundedCorners,
    textStyle: TextStyle = theme.typography.body,
    colors: RangeSliderColors = RangeSliderColors.defaultThemeColors(theme),
    onRangeUpdated: (SliderRange) -> Unit = {},
) {
    val inputBackgroundColor =
        if (isEnabled) colors.inputBackgroundColor
        else colors.disabledInputBackgroundColor
    val inputOutlineColor =
        if (isEnabled) colors.inputOutlineColor
        else colors.disabledInputOutlineColor

    val externalValue by rememberUpdatedState(value)
    var internalValue by remember { mutableStateOf(externalValue) }

    LaunchedEffect(externalValue) { internalValue = externalValue }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        SkydioRangeSlider(
            range = range,
            value = value,
            minSelectionSize = minSelectionSize,
            stepSize = stepSize,
            isEnabled = isEnabled,
            theme = theme,
            colors = colors,
            onRangeUpdated = {
                internalValue = it.start..it.endInclusive
                onRangeUpdated(internalValue)
            },
            modifier = Modifier.weight(1f))

        Spacer(modifier = Modifier.width(16.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(64.dp)
                .height(32.dp)
                .background(color = inputBackgroundColor, shape = inputShape)
                .border(width = 1.dp, color = inputOutlineColor, shape = inputShape)
        ) {
            BasicTextField(
                value = internalValue.start.toString(),
                enabled = isEnabled,
                textStyle = textStyle.copy(textAlign = TextAlign.Center),
                onValueChange = {
                    val newLow = it.toFloatOrNull() ?: internalValue.start
                    internalValue = newLow..internalValue.endInclusive
                    onRangeUpdated(internalValue)
                })
        }

        Spacer(modifier = Modifier.width(16.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(64.dp)
                .height(32.dp)
                .background(color = inputBackgroundColor, shape = inputShape)
                .border(width = 1.dp, color = inputOutlineColor, shape = inputShape)
        ) {
            BasicTextField(
                value = internalValue.endInclusive.toString(),
                enabled = isEnabled,
                textStyle = textStyle.copy(textAlign = TextAlign.Center),
                onValueChange = {
                    val newHigh = it.toFloatOrNull() ?: internalValue.endInclusive
                    internalValue = internalValue.start..newHigh
                    onRangeUpdated(internalValue)
                })
        }
    }
}

// MARK: Utils

typealias SliderRange = ClosedFloatingPointRange<Float>

// MARK: Preview/Example

@Preview
@Composable
private fun SkydioRangeSliderPreview() {
    ThemedPreviews { theme ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.background(color = theme.colors.defaultContainerBackgroundColor)
        ) {
            val colors = RangeSliderColors.defaultThemeColors(theme).copy(
                filledTrackColors = listOf(
                    Color(0xFF0E081A),
                    Color(0xFF0E1A85),
                    Color(0xFFFF1200),
                    Color(0xFFF7AB00),
                    Color(0xFFEDFF00),
                )
            )

            SkydioRangeSlider(modifier = Modifier, theme = theme, colors = colors) {}
            SkydioRangeSliderWithValue(stepSize = 1f, theme = theme, colors = colors) {}
            SkydioRangeSliderWithInput(stepSize = 1f, theme = theme, colors = colors) {}
        }
    }
}

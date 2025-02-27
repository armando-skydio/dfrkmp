package com.skydio.ui.components.widget.sliders

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.R
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme
import com.skydio.ui.util.ThemedPreviews

class SliderState {

    var isUserSliding: Boolean by mutableStateOf(false)

    companion object {
        internal val Saver: Saver<SliderState, *> = Saver(
            save = { it.isUserSliding },
            restore = { SliderState().apply { isUserSliding = it } })
    }

}

enum class ThumbSize(
    internal val size: Dp
) {
    Small(16.dp),
    Medium(24.dp),
    Large(32.dp)
}

@Composable
fun rememberSliderState(): SliderState =
    rememberSaveable(saver = SliderState.Saver) { SliderState() }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkydioSlider(
    modifier: Modifier = Modifier,
    // state
    sliderState: SliderState = rememberSliderState(),
    min: Float = 0f,
    max: Float = 100f,
    stepSize: Float = 1f,
    value: Float = (max - min) / 2,
    isEnabled: Boolean = true,
    // styling
    theme: AppTheme = getAppTheme(),
    thumbSize: ThumbSize = ThumbSize.Large,
    colors: SliderColors = SliderColors.defaultThemeColors(theme),
    onValueChanged: (Float) -> Unit = {},
) {
    assert(max >= min)

    val externalValue by rememberUpdatedState(value)
    var internalValue by remember { mutableFloatStateOf(value) }

    LaunchedEffect(externalValue) {
        if (!sliderState.isUserSliding) internalValue = externalValue.roundToStepSize(stepSize)
    }

    val safeStepSize = if (stepSize <= 0f) 1f else stepSize
    val steps = ((max - min) / safeStepSize).toInt()
    val sliderColors = SliderDefaults.colors(
        activeTrackColor = colors.filledTrackColor,
        disabledActiveTrackColor = colors.disabledFilledTrackColor,
        inactiveTrackColor = colors.trackColor,
        disabledInactiveTrackColor = colors.disabledTrackColor,
        thumbColor = colors.thumbColor,
        disabledThumbColor = colors.disabledThumbColor,
        activeTickColor = Color.Transparent,
        inactiveTickColor = Color.Transparent,
        disabledActiveTickColor = Color.Transparent,
        disabledInactiveTickColor = Color.Transparent
    )

    Slider(
        modifier = modifier,
        value = internalValue,
        enabled = isEnabled,
        valueRange = min..max,
        steps = steps,
        colors = sliderColors,
        onValueChange = {
            sliderState.isUserSliding = true
            internalValue = it.roundToStepSize(stepSize)
            onValueChanged(internalValue)
        },
        onValueChangeFinished = {
            sliderState.isUserSliding = false
            onValueChanged(internalValue)
        },
        thumb = {
            Image(
                painterResource(id = R.drawable.ic_slider_knob),
                contentDescription = null,
                modifier = Modifier.size(thumbSize.size),
            )
        }
    )
}

@Composable
fun SkydioSliderWithValue(
    modifier: Modifier = Modifier,
    // state
    min: Float = 0f,
    max: Float = 100f,
    stepSize: Float = 1f,
    value: Float = (max - min) / 2,
    isEnabled: Boolean = true,
    // styling
    theme: AppTheme = getAppTheme(),
    textStyle: TextStyle = theme.typography.body,
    colors: SliderColors = SliderColors.defaultThemeColors(theme),
    onValueChanged: (Float) -> Unit = {},
) {
    assert(max >= min)

    val externalValue by rememberUpdatedState(value)
    var internalValue by remember { mutableStateOf(value) }

    LaunchedEffect(externalValue) { internalValue = externalValue.roundToStepSize(stepSize) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        SkydioSlider(
            min = min,
            max = max,
            stepSize = stepSize,
            value = internalValue,
            isEnabled = isEnabled,
            theme = theme,
            colors = colors,
            onValueChanged = {
                internalValue = it.roundToStepSize(stepSize)
                onValueChanged(internalValue)
            },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = internalValue.toString(), style = textStyle)
    }
}

@Composable
fun SkydioSliderWithInput(
    modifier: Modifier = Modifier,
    // state
    min: Float = 0f,
    max: Float = 100f,
    stepSize: Float = 1f,
    value: Float = (max - min) / 2,
    isEnabled: Boolean = true,
    // styling
    theme: AppTheme = getAppTheme(),
    inputShape: Shape = theme.shapes.mediumRoundedCorners,
    textStyle: TextStyle = theme.typography.body,
    colors: SliderColors = SliderColors.defaultThemeColors(theme),
    onValueChanged: (Float) -> Unit = {},
) {
    assert(max >= min)

    val inputBackgroundColor =
        if (isEnabled) colors.inputBackgroundColor
        else colors.disabledInputBackgroundColor
    val inputOutlineColor =
        if (isEnabled) colors.inputOutlineColor
        else colors.disabledInputOutlineColor

    val externalValue by rememberUpdatedState(value)
    var internalValue by remember { mutableStateOf(value) }

    LaunchedEffect(externalValue) { internalValue = externalValue.roundToStepSize(stepSize) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        SkydioSlider(
            min = min,
            max = max,
            stepSize = stepSize,
            value = internalValue,
            isEnabled = isEnabled,
            theme = theme,
            colors = colors,
            modifier = Modifier.weight(1f),
            onValueChanged = {
                internalValue = it.roundToStepSize(stepSize)
                onValueChanged(internalValue)
            })

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
                value = internalValue.toString(),
                enabled = isEnabled,
                textStyle = textStyle.copy(textAlign = TextAlign.Center),
                onValueChange = {
                    internalValue = it.toFloatOrNull() ?: internalValue
                    onValueChanged(internalValue)
                })
        }
    }
}

// MARK: Preview/Example

@Preview
@Composable
private fun SkydioSliderPreview() {
    ThemedPreviews { theme ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .background(color = theme.colors.defaultContainerBackgroundColor)
                .padding(8.dp),
        ) {
            SkydioSlider(min = 0f, max = 100f, value = 50f, theme = theme)
            SkydioSliderWithValue(min = 0f, max = 100f, value = 50f, theme = theme)
            SkydioSliderWithInput(min = 0f, max = 100f, value = 50f, theme = theme)
        }
    }
}

package com.skydio.ui.components.widget.sliders

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType.Companion.Decimal
import androidx.compose.ui.text.input.KeyboardType.Companion.Number
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.widget.inputs.SkydioTextFieldWithEdgeText
import com.skydio.ui.components.widget.text.SkydioText
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme
import com.skydio.ui.util.ThemedPreviews

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SkydioSliderWithLabel(
    modifier: Modifier = Modifier,
    label: String,
    detail: String? = null,
    min: Float,
    max: Float,
    value: Float,
    units: String? = null,
    stepSize: Float = 1f,
    displayPrecision: Int? = null,
    enabled: Boolean = true,
    endPadding: Dp = 4.dp,
    onValueChanged: (Float) -> Unit = {},
    indicationContent: (@Composable RowScope.() -> Unit)? = null,
    thumbSize: ThumbSize = ThumbSize.Large,
    style: TextStyle = getAppTheme().typography.body,
    theme: AppTheme = getAppTheme()
) {

    val sliderState = rememberSliderState()

    fun stringValue(value: Float) = String.format("%.${displayPrecision ?: 0}f", value)

    var isUserEditingInput by remember { mutableStateOf(false) }
    val externalValue by rememberUpdatedState(value)

    // need 2 internal fields because they might not match, 4 example we allow an empty
    // EditTextValue while editing (until the keyboard is closed)
    var internalEditTextValueString by remember { mutableStateOf(stringValue(externalValue)) }
    var internalSliderValueString by remember { mutableStateOf(stringValue(externalValue)) }

    fun floatValue(value: String) =
        (removeCommasAndPeriods(value).toFloatOrNull() ?: externalValue).roundToStepSize(stepSize)

    val validateFunction: (String) -> String = {
        val newValue = floatValue(it).coerceIn(min, max)
        stringValue(newValue)
    }

    LaunchedEffect(
        key1 = isUserEditingInput,
        key2 = sliderState.isUserSliding,
        key3 = externalValue
    ) {
        if (isUserEditingInput.not() && sliderState.isUserSliding.not()) {
            internalEditTextValueString = stringValue(externalValue)
            internalSliderValueString = stringValue(externalValue)
        }
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SkydioText(text = label, modifier = Modifier.weight(1f), style = style)

            // NOTE(leo): this fixes size flickering SW-59882
            var minWidth by remember(internalEditTextValueString.length) { mutableStateOf(0.dp) }
            // NOTE(troy): this fixes the unbounded input length SW-68720
            val maxChars = 100

            indicationContent?.invoke(this)

            Box(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .padding(start = 4.dp, end = endPadding)
            ) {
                Box(modifier = Modifier.width(minWidth))

                val d = LocalDensity.current
                SkydioTextFieldWithEdgeText(
                    value = internalEditTextValueString,
                    enabled = enabled,
                    maxChars = maxChars,
                    modifier = Modifier
                        .onSizeChanged { with(d) { minWidth = it.width.toDp() } },
                    trailingText = units,
                    keyboardType = if (stepSize.rem(1) == 0f) Number else Decimal,
                    onValueChange = {
                        isUserEditingInput = true
                        // only updating the slider value
                        internalSliderValueString = validateFunction(it)
                    },
                    onValueChangeFinished = {
                        isUserEditingInput = false
                        if (sliderState.isUserSliding) {
                            return@SkydioTextFieldWithEdgeText
                        }

                        val newValue = floatValue(it).coerceIn(min, max)

                        if (externalValue == newValue) {
                            // don't trigger onValueChanged() if it wasn't actually changed
                            return@SkydioTextFieldWithEdgeText
                        }

                        internalEditTextValueString = stringValue(newValue)
                        internalSliderValueString = internalEditTextValueString
                        onValueChanged(newValue)
                    },
                    validateFunc = validateFunction
                )
            }
        }

        val k = LocalSoftwareKeyboardController.current
        SkydioSlider(
            sliderState = sliderState,
            min = min,
            max = max,
            value = floatValue(internalSliderValueString),
            stepSize = stepSize,
            theme = theme,
            isEnabled = enabled,
            thumbSize = thumbSize,
            onValueChanged = {
                internalSliderValueString = stringValue(it)
                internalEditTextValueString = internalSliderValueString
                if (isUserEditingInput.not()) {
                    onValueChanged(floatValue(internalEditTextValueString))
                }
                // if the keyboard was open then this should trigger onValueChangeFinished() in SkydioTextFieldWithEdgeText
                k?.hide()
            })

        if (detail.isNullOrBlank().not()) SkydioText(
            text = detail.orEmpty(),
            style = theme.typography.body,
            color = theme.colors.secondaryTextColor
        )
    }
}

fun removeCommasAndPeriods(input: String): String {
    val firstPeriodIndex = input.indexOf('.')

    // Remove all commas and periods except the first occurrence
    val cleanedInput = input.replace(",", "").replace(".", "").toMutableList()

    // Insert the first period back if it was present in the original input
    if (firstPeriodIndex != -1) {
        cleanedInput.add(firstPeriodIndex, '.')
    }

    return cleanedInput.joinToString("")
}

@Preview
@Composable
private fun TestSkydioSliderWithLabel() {
    ThemedPreviews { theme ->
        SkydioSliderWithLabel(
            modifier = Modifier.padding(0.dp),
            label = "Speed",
            min = 0f,
            max = 100f,
            value = 50f,
            theme = theme,
            displayPrecision = 1,
            stepSize = 0.5f,
            style = theme.typography.bodyLarge,
            enabled = true
        )

        Spacer(modifier = Modifier.height(5.dp))

        SkydioSliderWithLabel(
            label = "Speed",
            detail = "Speed Detail...",
            min = 0f,
            max = 100f,
            value = 50f,
            units = "mph",
            theme = theme,
            displayPrecision = 1,
            stepSize = 0.5f,
            style = theme.typography.bodyLarge,
            enabled = false
        )
    }
}

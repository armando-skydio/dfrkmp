package com.skydio.ui.components.widget.sliders

import androidx.compose.ui.graphics.Brush.Companion.horizontalGradient
import androidx.compose.ui.graphics.Color
import com.skydio.ui.designsystem.*

/**
 * Color definition class for [SkydioSlider]s.
 */
data class SliderColors(
    val trackColor: Color,
    val disabledTrackColor: Color,
    val filledTrackColor: Color,
    val disabledFilledTrackColor: Color,
    val thumbColor: Color,
    val disabledThumbColor: Color,
    val inputBackgroundColor: Color,
    val disabledInputBackgroundColor: Color,
    val inputOutlineColor: Color,
    val disabledInputOutlineColor: Color,
) {
    companion object {
        fun defaultThemeColors(
            theme: AppTheme,
            trackColor: Color = theme.colors.appBackgroundColor,
            disabledTrackColor: Color = theme.colors.disabledTextColor,
            filledTrackColor: Color = Blue500,
            disabledFilledTrackColor: Color = Blue200,
            thumbColor: Color = Blue500,
            disabledThumbColor: Color = Blue200,
            inputBackgroundColor: Color = theme.colors.defaultWidgetBackgroundColor,
            disabledInputBackgroundColor: Color = theme.colors.defaultWidgetBackgroundColor,
            inputOutlineColor: Color = when (theme) {
                AppTheme.Dark -> Gray700
                AppTheme.Light -> Gray300
            },
            disabledInputOutlineColor: Color = inputOutlineColor,
        ) = SliderColors(
            trackColor = trackColor,
            disabledTrackColor = disabledTrackColor,
            filledTrackColor = filledTrackColor,
            disabledFilledTrackColor = disabledFilledTrackColor,
            thumbColor = thumbColor,
            disabledThumbColor = disabledThumbColor,
            inputBackgroundColor = inputBackgroundColor,
            disabledInputBackgroundColor = disabledInputBackgroundColor,
            inputOutlineColor = inputOutlineColor,
            disabledInputOutlineColor = disabledInputOutlineColor,
        )
    }
}

/**
 * Color definition class for [SkydioRangeSlider]s.
 */
data class RangeSliderColors(
    val trackColors: List<Color>,
    val disabledTrackColors: List<Color>,
    val filledTrackColors: List<Color>,
    val disabledFilledTrackColors: List<Color>,
    val thumbColor: Color,
    val thumbOutlineColor: Color,
    val disabledThumbColor: Color,
    val disabledThumbOutlineColor: Color,
    val inputBackgroundColor: Color,
    val disabledInputBackgroundColor: Color,
    val inputOutlineColor: Color,
    val disabledInputOutlineColor: Color,
) {

    private fun List<Color>.ensureGradient(): List<Color> =
        if (this.size > 1) this
        else if (size == 1) listOf(this.first(), this.first())
        else throw IllegalArgumentException("colors list must have at least one color")

    val trackGradient = horizontalGradient(trackColors.ensureGradient())
    val disabledTrackGradient = horizontalGradient(disabledTrackColors.ensureGradient())
    val filledTrackGradient = horizontalGradient(filledTrackColors.ensureGradient())
    val disabledFilledTrackGradient = horizontalGradient(disabledFilledTrackColors.ensureGradient())

    companion object {
        fun defaultThemeColors(
            theme: AppTheme,
            trackColors: List<Color> = listOf(theme.colors.appBackgroundColor),
            disabledTrackColors: List<Color> = listOf(theme.colors.disabledTextColor),
            filledTrackColors: List<Color> = listOf(Blue500),
            disabledFilledTrackColors: List<Color> = listOf(Blue200),
            thumbColor: Color = theme.colors.primaryTextColor,
            thumbOutlineColor: Color = Gray700,
            disabledThumbColor: Color = theme.colors.disabledTextColor,
            disabledThumbOutlineColor: Color = Gray700,
            inputBackgroundColor: Color = theme.colors.defaultWidgetBackgroundColor,
            disabledInputBackgroundColor: Color = theme.colors.defaultWidgetBackgroundColor,
            inputOutlineColor: Color = when (theme) {
                AppTheme.Dark -> Gray700
                AppTheme.Light -> Gray300
            },
            disabledInputOutlineColor: Color = inputOutlineColor,
        ) = RangeSliderColors(
            trackColors = trackColors,
            disabledTrackColors = disabledTrackColors,
            filledTrackColors = filledTrackColors,
            disabledFilledTrackColors = disabledFilledTrackColors,
            thumbColor = thumbColor,
            thumbOutlineColor = thumbOutlineColor,
            disabledThumbColor = disabledThumbColor,
            disabledThumbOutlineColor = disabledThumbOutlineColor,
            inputBackgroundColor = inputBackgroundColor,
            disabledInputBackgroundColor = disabledInputBackgroundColor,
            inputOutlineColor = inputOutlineColor,
            disabledInputOutlineColor = disabledInputOutlineColor,
        )
    }
}

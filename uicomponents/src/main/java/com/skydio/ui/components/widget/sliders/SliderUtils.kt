package com.skydio.ui.components.widget.sliders

import kotlin.math.pow
import kotlin.math.roundToInt

fun Float.roundToStepSize(stepSize: Float): Float =
    if (stepSize == Float.NEGATIVE_INFINITY) this
    else {
        val result = (this / stepSize).roundToInt() * stepSize
        val numDecimalPlaces = stepSize.toString().substringAfter('.').length
        val scaleFactor = 10.0.pow(numDecimalPlaces).toFloat()
        (result * scaleFactor).roundToInt() / scaleFactor
    }

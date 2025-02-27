package com.skydio.ui.util

fun RangeSliderRangeHolder.isRangeValid(): Boolean {
    if (sliderAbsMax.isNaN() || sliderAbsMin.isNaN()) {
        return false
    }
    if (sliderCurMax.isNaN() || sliderCurMin.isNaN()) {
        return false
    }
    if (sliderCurMax > sliderAbsMax) {
        return false
    }
    if (sliderCurMin < sliderAbsMin) {
       return false
    }

    return true
}

interface RangeSliderRangeHolder {

    val sliderAbsMin: Float

    val sliderAbsMax: Float

    val sliderCurMin: Float

    val sliderCurMax: Float

}

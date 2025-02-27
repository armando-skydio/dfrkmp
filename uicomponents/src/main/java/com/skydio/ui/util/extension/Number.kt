package com.skydio.ui.util.extension

import android.content.Context
import android.util.TypedValue

fun Number.convertDpToPx(context: Context): Float {
    val displayMetrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), displayMetrics)
}

fun Number.convertDpToPxInt(context: Context): Int {
    return convertDpToPx(context).toInt()
}

fun Float.round(decimals: Int = 2): String = "%.${decimals}f".format(this)

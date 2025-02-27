package com.skydio.ui.util

import androidx.annotation.DimenRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.skydio.ui.components.R

private val totalNumColumns: Int
    @Composable
    inline get() = integerResource(R.integer.total_screen_columns)

private val columnWidthDp: Int
    @Composable
    inline get() = LocalConfiguration.current.screenWidthDp / totalNumColumns

/**
 * Utility extensions for specifying dimensions based in design system columns.
 */
val Int.col: Dp
    @Composable
    get() = (columnWidthDp * this).dp

/**
 * Utility extensions for specifying dimensions based in design system columns.
 */
val Float.col: Dp
    @Composable
    get() = (columnWidthDp * this).dp

/**
 * Utility extensions for specifying dimensions based in design system columns.
 */
val Double.col: Dp
    @Composable
    get() = (columnWidthDp * this).dp

/**
 * Loads a float representing a number of columns from the provided [DimenRes] and then
 * converts that to [Dp] for use in UI sizes.
 */
@Composable
fun columnsResource(@DimenRes numColsResId: Int) = with(LocalContext.current) {
    ResourcesCompat.getFloat(resources, numColsResId).col
}

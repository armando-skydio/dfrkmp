package com.skydio.ui.components.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme

/**
 * Helper for row dividers, because default compose doesn't have this.
 */
@Composable
fun RowScope.RowDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    theme: AppTheme = getAppTheme(),
    color: Color = theme.colors.dividerColor,
) {
    Box(modifier = modifier
        .fillMaxHeight()
        .width(thickness)
        .background(color = color))
}

@Composable
fun ColumnDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    theme: AppTheme = getAppTheme(),
    color: Color = theme.colors.dividerColor,
) {
    Box(modifier = modifier
        .fillMaxWidth()
        .height(thickness)
        .background(color)
    )
}
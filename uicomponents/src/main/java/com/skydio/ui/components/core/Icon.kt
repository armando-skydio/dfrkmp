package com.skydio.ui.components.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme
import com.skydio.ui.util.ImageSource

/**
 * Helper function for [Image] that automatically tints the icon based on theme.
 * This should be the favored way of defining an icon.
 */
@Composable
fun Icon(
    source: ImageSource,
    modifier: Modifier = Modifier,
    alpha: Float = DefaultAlpha,
    contentScale: ContentScale = ContentScale.Inside,
    theme: AppTheme = getAppTheme(),
    tintColor: Color = theme.colors.primaryTextColor
) = Image(
    source = source,
    modifier = modifier,
    alpha = alpha,
    contentScale = contentScale,
    colorFilter = ColorFilter.tint(color = tintColor)
)

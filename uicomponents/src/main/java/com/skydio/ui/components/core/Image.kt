package com.skydio.ui.components.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import com.skydio.ui.util.ImageSource

/**
 * Helper function for [androidx.compose.foundation.Image] that uses an [ImageSource].
 */
@Composable
fun Image(
    source: ImageSource,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) = androidx.compose.foundation.Image(
    painter = source.asPainter(),
    contentDescription = source.contentDescription,
    modifier = modifier,
    alignment = alignment,
    contentScale = contentScale,
    alpha = alpha,
    colorFilter = colorFilter
)

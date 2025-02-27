package com.skydio.ui.components.widget.indicators

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.skydio.ui.designsystem.*

// MARK: Composable Impl

@Composable
fun SkydioLoadingIndicator(
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 4.dp,
    theme: AppTheme = getAppTheme(),
    colors: LoadingIndicatorColors = LoadingIndicatorColors.defaultThemeColors(theme)
) {
    Box(modifier = modifier.aspectRatio(1f)) {
        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                drawCircle(
                    color = colors.ringColor,
                    style = Stroke(width = strokeWidth.toPx())
                )
            }
        )

        val infiniteTransition = rememberInfiniteTransition()
        val angle by infiniteTransition.animateFloat(
            initialValue = 0F,
            targetValue = 360F,
            animationSpec = infiniteRepeatable(animation = tween(1000, easing = LinearEasing))
        )

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .rotate(angle)
        ) {
            drawArc(
                brush = SolidColor(colors.strokeColor),
                startAngle = -90f,
                sweepAngle = 90f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx())
            )
        }
    }
}

// MARK: Theming

data class LoadingIndicatorColors(
    val ringColor: Color,
    val strokeColor: Color,
) {
    companion object {
        fun defaultThemeColors(
            theme: AppTheme,
            ringColor: Color = when (theme) {
                AppTheme.Dark -> Gray800
                AppTheme.Light -> Gray200
            },
            strokeColor: Color = Blue500,
        ) = LoadingIndicatorColors(
            ringColor = ringColor,
            strokeColor = strokeColor,
        )
    }
}

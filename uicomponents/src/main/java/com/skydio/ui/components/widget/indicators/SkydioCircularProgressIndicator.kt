package com.skydio.ui.components.widget.indicators

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Circular Progress Bar with customizable thickness and colors
 * from 0..360
 */
@Composable
fun SkydioCircularProgressIndicator(
    strokeWidth: Dp = 4.dp,
    ringColor: Color = Color.Transparent,
    strokeColor: Color = Color.White,
    durationMillis: Int = 5000,
    initialValue: Int = 0, // 0..360
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Canvas(
            modifier = modifier.fillMaxSize(),
            onDraw = {
                drawCircle(
                    color = ringColor,
                    style = Stroke(width = strokeWidth.toPx())
                )
            }
        )
        val initial by rememberUpdatedState(initialValue)
        val duration by rememberUpdatedState(durationMillis)

        val offsetMillis by remember { derivedStateOf { initial * duration / 360 } }

        key(offsetMillis) {
            val infiniteTransition = rememberInfiniteTransition(label = "ProgressIndicator")
            val angle by infiniteTransition.animateFloat(
                label = "ProgressIndicatorLoading",
                initialValue = 0F,
                targetValue = 360F,
                animationSpec = infiniteRepeatable(
                    animation = tween(duration, easing = LinearEasing),
                    initialStartOffset = StartOffset(-offsetMillis)
                )
            )

            Canvas(
                modifier = modifier.fillMaxSize()
            ) {
                drawArc(
                    brush = SolidColor(strokeColor),
                    startAngle = -90f,
                    sweepAngle = angle,
                    useCenter = false,
                    style = Stroke(width = strokeWidth.toPx())
                )
            }
        }
    }
}

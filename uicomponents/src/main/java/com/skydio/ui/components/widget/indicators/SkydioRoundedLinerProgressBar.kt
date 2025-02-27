package com.skydio.ui.components.widget.indicators

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.getAppTheme


@Composable
fun SkydioRoundedLinerProgressBar(
    progress: Float,
    progressColor: Color,
    modifier: Modifier = Modifier,
    theme: AppTheme = getAppTheme(),
) = if (progress in 0f..1f) {
    LinearProgressIndicator(
        progress = progress,
        strokeCap = StrokeCap.Round,
        color = progressColor,
        trackColor = Color.Black.copy(0.9F),
        modifier = modifier.roundedStyle(theme),
    )
} else {
    // Indeterminate State
    LinearProgressIndicator(
        strokeCap = StrokeCap.Round,
        color = progressColor,
        trackColor = Color.Black.copy(0.9F),
        modifier = modifier.roundedStyle(theme),
    )
}

private fun Modifier.roundedStyle(
    theme: AppTheme,
) = this
    .zIndex(1.0F)
    .padding(vertical = 8.dp)
    .height(8.dp)
    .shadow(elevation = 4.dp)
    .clip(theme.shapes.mediumRoundedCorners)

const val INDETERMINATE_PROGRESS = -1f

val progressSuccessColor = Color(0xFF4ADE80)
val progressErrorColor = Color(0xFFF53D3D)
val progressIncompleteColor = Color.White

package com.skydio.ui.designsystem

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

/**
 * Class (with defaults) for defining UI shapes which can, through the use of extension functions,
 * define theme-specific shapes.
 */
data class SkydioShapes(
    val parentTheme: AppTheme,
    val smallRoundedCorners: RoundedCornerShape = RoundedCornerShape(2.dp),
    val mediumRoundedCorners: RoundedCornerShape = RoundedCornerShape(4.dp),
    val largeRoundedCorners: RoundedCornerShape = RoundedCornerShape(8.dp),
    val xlargeRoundedCorners: RoundedCornerShape = RoundedCornerShape(16.dp),
    val xxlargeRoundedCorners: RoundedCornerShape = RoundedCornerShape(24.dp),

    val smallStartRoundedCorners: RoundedCornerShape = RoundedCornerShape(2.dp, 0.dp, 0.dp, 2.dp),
    val smallEndRoundedCorners: RoundedCornerShape = RoundedCornerShape(0.dp, 2.dp, 2.dp, 0.dp),
    val noRoundedCorners: RoundedCornerShape = RoundedCornerShape(0.dp)
)

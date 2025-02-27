package com.skydio.ui.util

import androidx.compose.ui.MotionDurationScale

/**
 * on C78 Settings.Global.ANIMATOR_DURATION_SCALE is set to 0 (this is to prevent accidentally
 * seeing power cycle dialog in the middle of the app),
 * but it all the composable animations failed to execute because of this.
 * This class is a workaround for that.
 * I found the solution here https://issuetracker.google.com/issues/265177763
 */
object FixedMotionDurationScale : MotionDurationScale {
    override val scaleFactor: Float
        get() = 1f
}

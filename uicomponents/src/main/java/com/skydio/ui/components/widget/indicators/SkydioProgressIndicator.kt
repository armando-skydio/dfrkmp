package com.skydio.ui.components.widget.indicators

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.skydio.ui.components.widget.AbstractSkydioComposeWidget
import com.skydio.ui.designsystem.AppTheme
import com.skydio.ui.designsystem.Blue400
import com.skydio.ui.designsystem.Blue500
import com.skydio.ui.designsystem.Gray0
import com.skydio.ui.designsystem.Gray100
import com.skydio.ui.designsystem.Gray300
import com.skydio.ui.designsystem.Gray400
import com.skydio.ui.designsystem.Gray600
import com.skydio.ui.designsystem.Gray800
import com.skydio.ui.designsystem.Green400
import com.skydio.ui.designsystem.Green500
import com.skydio.ui.designsystem.Red400
import com.skydio.ui.designsystem.getAppTheme

// MARK: View Impl

/**
 * [AbstractSkydioComposeWidget] implementation of a progress indicator.
 * As with all [AbstractSkydioComposeWidget]s this is just a wrapper around a compose function.
 */
class SkydioProgressIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defaultStyleAttr: Int = 0
) : AbstractSkydioComposeWidget<SkydioProgressIndicator.State>(
    context,
    attrs,
    defaultStyleAttr,
    defaultViewState = State()
) {

    /**
     * The entire state of any given [SkydioProgressIndicator].
     */
    data class State(
        val progressPercent: Float = 0f,
        val detailedProgress: DetailedProgress? = null,
        val isPaused: Boolean = false,
        val errorMessage: String? = null,
        override var isEnabled: Boolean = true,
        override var isVisible: Boolean = true,
    ) : BaseViewState() {

        data class DetailedProgress(val leftText: String?, val rightText: String?)

        internal val progressPercentageText = "${(100 * progressPercent).toInt()}%"
    }

    @Composable
    override fun Content(state: State) = SkydioProgressIndicator(state = state)

}

// MARK: Composable Impl

@Composable
fun SkydioProgressIndicator(
    state: ProgressIndicatorState,
    modifier: Modifier = Modifier,
    theme: AppTheme = getAppTheme()
) = SkydioProgressIndicator(
    progressPercent = state.progressPercent,
    isPaused = state.isPaused,
    leftText = state.detailedProgress?.leftText ?: state.progressPercentageText,
    rightText = state.detailedProgress?.rightText,
    errorMessage = state.errorMessage,
    modifier = modifier,
    theme = theme
)

@Composable
fun SkydioProgressIndicator(
    progressPercent: Float,
    modifier: Modifier = Modifier,
    isPaused: Boolean = false,
    leftText: String? = null,
    rightText: String? = null,
    errorMessage: String? = null,
    theme: AppTheme = getAppTheme(),
    colors: ProgressIndicatorColors = ProgressIndicatorColors.defaultThemeColors(theme),
    detailTextStyle: TextStyle = theme.typography.body,
    errorTextStyle: TextStyle = theme.typography.body,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
) {
    if (progressPercent < 0) {
        // indeterminate state
        Box(modifier = modifier) {
            LinearProgressIndicator(
                color = colors.progressIncompleteColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.progressDoneColor)
                    .height(4.dp)
            )
        }
        return
    }
    Column(modifier = modifier, verticalArrangement = verticalArrangement) {
        val lineColor = when {
            errorMessage != null -> colors.progressDoneErrorColor
            progressPercent == 1f -> colors.progressFinishedColor
            isPaused -> colors.progressDonePausedColor
            else -> colors.progressDoneColor
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
        ) {

            drawLine(
                color = colors.progressIncompleteColor,
                start = Offset(x = 0f, y = size.height / 2),
                end = Offset(x = size.width, y = size.height / 2),
                strokeWidth = 6.dp.toPx(),
            )

            drawLine(
                color = lineColor,
                start = Offset(x = 0f, y = size.height / 2),
                end = Offset(x = size.width * progressPercent, y = size.height / 2),
                strokeWidth = 6.dp.toPx(),
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (leftText != null || rightText != null)
            Row(modifier = modifier.height(20.dp)) {
                ProvideTextStyle(value = detailTextStyle.copy(color = colors.detailTextColor)) {
                    if (leftText != null) Text(text = leftText)
                    Spacer(modifier = Modifier.weight(1f))
                    if (rightText != null) Text(text = rightText)
                }
            }

        if (errorMessage != null)
            Column(modifier = modifier.height(20.dp)) {
                ProvideTextStyle(value = errorTextStyle.copy(color = colors.errorTextColor)) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = errorMessage)
                }
            }
    }
}

// MARK: Utils

typealias ProgressIndicatorState = SkydioProgressIndicator.State

typealias ProgressIndicatorDetail = SkydioProgressIndicator.State.DetailedProgress

// MARK: Theming

data class ProgressIndicatorColors(
    val detailTextColor: Color,
    val errorTextColor: Color,
    val progressIncompleteColor: Color,
    val progressDoneColor: Color,
    val progressDonePausedColor: Color,
    val progressDoneErrorColor: Color,
    val progressFinishedColor: Color,
) {
    companion object {
        fun defaultThemeColors(
            theme: AppTheme,
            detailTextColor: Color = when (theme) {
                AppTheme.Dark -> Gray0
                AppTheme.Light -> Gray600
            },
            errorTextColor: Color = when (theme) {
                AppTheme.Dark -> Gray400
                AppTheme.Light -> Gray600
            },
            progressIncompleteColor: Color = when (theme) {
                AppTheme.Dark -> Gray800
                AppTheme.Light -> Gray100
            },
            progressDoneColor: Color = when (theme) {
                AppTheme.Dark -> Blue400
                AppTheme.Light -> Blue500
            },
            progressDonePausedColor: Color = when (theme) {
                AppTheme.Dark -> Gray300
                AppTheme.Light -> Gray600
            },
            progressDoneErrorColor: Color = Red400,
            progressFinishedColor: Color = when (theme) {
                AppTheme.Dark -> Green400
                AppTheme.Light -> Green500
            },
        ) = ProgressIndicatorColors(
            detailTextColor = detailTextColor,
            errorTextColor = errorTextColor,
            progressIncompleteColor = progressIncompleteColor,
            progressDoneColor = progressDoneColor,
            progressDonePausedColor = progressDonePausedColor,
            progressDoneErrorColor = progressDoneErrorColor,
            progressFinishedColor = progressFinishedColor,
        )
    }
}
